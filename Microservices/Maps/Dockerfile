FROM maven:3.9.8-eclipse-temurin-21 AS build

ENV HOME=/usr/app
RUN mkdir p- ${HOME}
WORKDIR ${HOME}

COPY src ${HOME}/src
COPY pom.xml ${HOME}

RUN --mount=type=cache,target=/root/.m2 mvn -f ${HOME}/pom.xml clean package

FROM openjdk:21-slim

WORKDIR /app

COPY --from=build /usr/app/target/MS-Maps-0.0.1-SNAPSHOT.jar /app/my-app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/my-app.jar"]
