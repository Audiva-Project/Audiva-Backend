FROM eclipse-temurin:21-jdk-alpine

LABEL maintainer="Admin"

WORKDIR /app

COPY target/audiva-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]