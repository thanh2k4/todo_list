pipeline {
    agent any
    environment {
        DOCKER_REGISTRY = "thanh2k4"
        IMAGE_NAME_FRONTEND = "todo-frontend"
        IMAGE_NAME_BACKEND = "todo-backend"
    }
    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/your-repo/todolist-app.git'
            }
        }
        stage('Build and Test Frontend') {
            steps {
                dir('frontend') {
                    sh 'npm install'
                    sh 'npm run test'
                    sh 'npm run build'
                }
            }
        }
        stage('Build and Test Backend') {
            steps {
                dir('backend') {
                    sh './mvnw clean package'
                    sh './mvnw test'
                }
            }
        }
        stage('Build Docker Images') {
            steps {
                sh 'docker build -t $DOCKER_REGISTRY/$IMAGE_NAME_FRONTEND ./frontend'
                sh 'docker build -t $DOCKER_REGISTRY/$IMAGE_NAME_BACKEND ./backend'
            }
        }
        stage('Push to Docker Hub') {
            steps {
                withDockerRegistry([ credentialsId: 'dockerhub-credentials', url: '' ]) {
                    sh 'docker push $DOCKER_REGISTRY/$IMAGE_NAME_FRONTEND'
                    sh 'docker push $DOCKER_REGISTRY/$IMAGE_NAME_BACKEND'
                }
            }
        }
        stage('Deploy with Docker Compose') {
            steps {
                sh 'docker-compose down'
                sh 'docker-compose up -d'
            }
        }
    }
    post {
        always {
            echo 'Pipeline finished.'
        }
        success {
            echo 'Deployment successful!'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}