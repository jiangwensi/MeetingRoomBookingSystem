FROM node:alpine
WORKDIR "/mrbs"
COPY ./package.json ./
RUN npm install
COPY . .
EXPOSE 3000
ENTRYPOINT ["npm","run","start"]