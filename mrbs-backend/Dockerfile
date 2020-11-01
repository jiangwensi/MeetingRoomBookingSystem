FROM openjdk:8
WORKDIR "/mrbs"
ARG JAR_FILE=target/*jar
COPY ${JAR_FILE} ./app.jar
EXPOSE 8080
CMD ["java","-jar","./app.jar"]
#ENTRYPOINT ["java","-jar","./app.jar","--spring.datasource.url=jdbc:mysql://mrbs.codaarc5fszh.ap-southeast-1.rds.amazonaws.com:3306/mrbs?serverTimezone=GMT%2B8","--spring.datasource.password=wensi","--spring.datasource.username=wensi","--logging.file.path=/mrbs/logs/"]