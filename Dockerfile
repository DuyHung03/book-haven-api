#FROM maven:3.8.2-jdk-11 AS build
#COPY . .
#RUN mvn clean package -DskipTests
#
#FROM openjdk:11-jdk-slim
#COPY --from=build /target/*.jar book-store-api.jar
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","book-store-api.jar"]

FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]