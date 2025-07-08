FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY nexora-crypto-api/pom.xml .
COPY nexora-crypto-api/src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]
