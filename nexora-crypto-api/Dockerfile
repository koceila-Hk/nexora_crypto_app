# Use an official Maven image as the base image
FROM maven:3.9.6-eclipse-temurin-17 AS builder
# Set the working directory in the container
WORKDIR /build
# Copy the pom.xml and the project files to the container
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
# Build the application using Maven
RUN mvn clean package -DskipTests
# Use an official OpenJDK image as the base image
FROM eclipse-temurin:17-jdk-alpine
# Set the working directory in the container
WORKDIR /app
# Copy the built JAR file from the previous stage to the container
COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080
# Set the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]
