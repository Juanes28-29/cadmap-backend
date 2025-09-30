# Etapa 1: Construcción del JAR
FROM gradle:8.10-jdk17 AS builder
WORKDIR /app
COPY . .
RUN ./gradlew clean shadowJar

# Etapa 2: Imagen final
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*-all.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]