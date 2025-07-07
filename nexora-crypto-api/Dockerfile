# build
FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# runtime
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

COPY target/*.jar app.jar

RUN apt-get update && apt-get install -y default-mysql-client && apt-get clean

EXPOSE 8080

CMD ["/wait-for-it.sh", "-t", "60", "mysql:3306", "--", "java", "-jar", "/app/app.jar"]

