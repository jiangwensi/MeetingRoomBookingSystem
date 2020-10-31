FROM openjdk:8
WORKDIR "/mrbs"
ARG JAR_FILE=target/*jar
COPY ${JAR_FILE} ./app.jar
EXPOSE 8080
CMD ["java","-jar","./app.jar"]
#ENTRYPOINT ["java","-jar","./app.jar","--spring.config.additional-location=./config/local.application.properties"]
#CMD java -jar ./app.jar || tail -f /dev/null