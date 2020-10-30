FROM node:alpine
WORKDIR "/mrbs"
COPY ./package.json ./
RUN npm install
COPY . .
EXPOSE 3000
RUN ["npm","run","build"]