# Etapa de build com Java 17
FROM maven:3.9.6-eclipse-temurin-17 AS build

COPY src /app/src
COPY pom.xml /app

WORKDIR /app
RUN mvn clean install -DskipTests

# Etapa de execução com Java 17 (imagem slim)
FROM openjdk:17-jdk

COPY --from=build /app/target/alpha-0.0.1-SNAPSHOT.jar /app/app.jar

WORKDIR /app

EXPOSE 8080

CMD ["java","-jar","app.jar"]