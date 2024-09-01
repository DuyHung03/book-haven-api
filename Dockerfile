# Build stage
FROM maven:3.8.3-openjdk-17 AS build

# Set the working directory
WORKDIR /app

# Copy the Maven POM file and source code
COPY pom.xml .
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:17.0.1-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/book-store-api-0.0.1-SNAPSHOT.jar book-store-api.jar

# Expose the port on which the application will run
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "book-store-api.jar"]
