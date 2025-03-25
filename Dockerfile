FROM gradle:7.6-jdk21 AS builder
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew clean build -x test

FROM openjdk:21-jdk-slim AS final
WORKDIR /app
COPY --from=builder /app/build/libs/aes-gcm-masking-be-1.0.0.jar /app/app.jar
COPY src/main/resources/application.yaml /app/application.yaml

CMD ["java", "-Dspring.config.location=file:application.yaml", "-jar", "/app/app.jar"]
EXPOSE 8080