FROM maven:3.6.1-jdk-11 as build

WORKDIR /opt

COPY *pom.xml /opt/

COPY . /opt/

RUN mvn package -DskipTests

#FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim as run

FROM openjdk:11-jdk-slim

COPY --from=build /opt/target/crawler-*.jar /opt/app.jar

CMD java -jar /opt/app.jar --spring.profiles.active=prod