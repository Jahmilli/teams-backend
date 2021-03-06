# To build, run: docker build -t teams-backend .
# TODO: Use a smaller image as the base image
FROM openjdk:14-jdk-buster

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]
