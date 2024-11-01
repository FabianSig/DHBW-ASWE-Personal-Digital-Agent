FROM maven:3.9.8-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml /app/
COPY src /app/src

RUN mvn clean package

FROM openjdk:21

WORKDIR /app

COPY --from=build /app/target/Personal-Digital-Agent-MS-Speisekarte-0.0.1.jar /app/my-app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/my-app.jar"]
