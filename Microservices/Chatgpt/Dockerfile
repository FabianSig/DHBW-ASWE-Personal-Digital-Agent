FROM maven:3.8.7-amazoncorretto:21-al2023 AS build

WORKDIR /app

COPY pom.xml /app/
COPY src /app/src

RUN mvn clean package

FROM openjdk:21

WORKDIR /app

COPY --from=build /app/target/MS-ChatGPT-0.0.1-SNAPSHOT.jar /app/my-app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/my-app.jar"]
