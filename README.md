# Teams Backend

## Description

This is the backend for the teams like application we're building as part of Software Engineering Studio 2B Autumn 2020.

This is a Springboot application that will provide a RESTful interface for the Teams UI to interact with.

## Setting up for Development

To run this application you will need:

- JDK-14

## Building the Application

To build this application, you can do so utilising the Gradle Wrapper by running `./gradlew build`

To build as Docker Image, run: `docker build -t teams-backend .`

## Running the Application

Once the application has been built, you can run: `java -jar build/libs/teams-backend-0.0.1-SNAPSHOT.jar`
