
FROM node:22.9.0-slim 
WORKDIR /app

COPY package.json /app
COPY package-lock.json /app

RUN npm install

COPY . /app

RUN npm run build

RUN npm install serve

CMD ["serve", "-s", "build", "-l", "3000"]
