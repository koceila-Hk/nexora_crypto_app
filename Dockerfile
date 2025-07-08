FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app
COPY ./nexora-crypto-api/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]
