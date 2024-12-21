#!/bin/bash


GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' 

check_health() {
    local service=$1
    local color=$2
    local max_attempts=30
    local attempt=1

    echo "Checking health of $service-$color..."
    
    while [ $attempt -le $max_attempts ]; do
        if docker-compose ps "$service-$color" | grep -q "Up"; then

            if [ "$service" = "backend" ]; then
                if curl -s "http://localhost:8080/actuator/health" | grep -q "UP"; then
                    return 0
                fi
            else
                if curl -s "http://localhost:80" > /dev/null; then
                    return 0
                fi
            fi
        fi
        
        echo "Attempt $attempt of $max_attempts..."
        sleep 2
        ((attempt++))
    done
    
    return 1
}


switch_color() {
    local service=$1
    local current_color=$(docker-compose ps | grep "$service-" | grep "Up" | awk '{print $1}' | cut -d'-' -f2)
    local new_color

    if [ "$current_color" == "blue" ]; then
        new_color="green"
    else
        new_color="blue"
    fi

    echo -e "${GREEN}Switching $service from $current_color to $new_color${NC}"


    cp nginx.conf nginx.conf.backup


    sed -i "s/$service-$current_color/$service-$new_color/" nginx.conf


    docker-compose exec nginx nginx -s reload


    if [ $? -ne 0 ]; then
        echo -e "${RED}Failed to reload nginx. Rolling back to previous configuration...${NC}"
        mv nginx.conf.backup nginx.conf
        docker-compose exec nginx nginx -s reload
        return 1
    fi

    rm nginx.conf.backup
    echo -e "${GREEN}$service switched to $new_color${NC}"
    return 0
}


deploy() {
    local service=$1
    local color=$2

    echo -e "${GREEN}Deploying new version of $service to $color${NC}"


    docker-compose up -d --no-deps --build "$service-$color"


    if ! check_health "$service" "$color"; then
        echo -e "${RED}Deployment failed! New version is not healthy.${NC}"
        return 1
    fi

    echo -e "${GREEN}New version of $service deployed successfully to $color${NC}"
    return 0
}


rollback() {
    local service=$1
    local color=$2

    echo -e "${RED}Rolling back $service to $color${NC}"
    

    switch_color "$service"
    
    echo -e "${GREEN}Rollback completed${NC}"
}


main() {
    local service=$1


    if [[ ! "$service" =~ ^(frontend|backend)$ ]]; then
        echo -e "${RED}Invalid service name. Use 'frontend' or 'backend'${NC}"
        exit 1
    fi


    local current_color=$(docker-compose ps | grep "$service-" | grep "Up" | awk '{print $1}' | cut -d'-' -f2)
    local target_color

    if [ "$current_color" == "blue" ]; then
        target_color="green"
    else
        target_color="blue"
    fi


    if ! deploy "$service" "$target_color"; then
        echo -e "${RED}Deployment failed. Starting rollback...${NC}"
        rollback "$service" "$current_color"
        exit 1
    fi


    if ! switch_color "$service"; then
        echo -e "${RED}Failed to switch traffic. Starting rollback...${NC}"
        rollback "$service" "$current_color"
        exit 1
    fi

    echo -e "${GREEN}Deployment of $service completed successfully${NC}"
}


if [ $# -eq 0 ]; then
    echo -e "${RED}Please provide a service name (frontend or backend)${NC}"
    exit 1
fi


main $1

