# Étape de build
FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Étape de runtime
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/nexora-crypto-api-0.0.1-SNAPSHOT.jar app.jar

RUN apt-get update && apt-get install -y default-mysql-client && apt-get clean
EXPOSE 8088
CMD ["java", "-jar", "app.jar"]
