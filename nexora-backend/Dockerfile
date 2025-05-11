FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/nexora-backend-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

RUN apt-get update && apt-get install -y default-mysql-client

ENTRYPOINT ["java", "-jar", "/app.jar"]
