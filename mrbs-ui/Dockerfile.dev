FROM node:alpine
WORKDIR "/mrbs"
COPY ./package.json ./
RUN npm install
COPY . .
ENTRYPOINT ["npm","run","start"]