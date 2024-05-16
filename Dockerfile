FROM maven:3.9.6-jdk-8 AS build

COPY src /app/src
COPY pom.xml /app

WORKDIR /app