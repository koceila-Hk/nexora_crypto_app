
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /build
COPY nexora-crypto-api /build

RUN mvn clean package -DskipTests


FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]
