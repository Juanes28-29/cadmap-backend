# Etapa de compilación
FROM gradle:8.5-jdk21 AS build
WORKDIR /app
COPY . .
# Ejecuta los tests y luego genera el shadowJar
RUN ./gradlew clean test shadowJar

# Etapa de ejecución
FROM eclipse-temurin:21-jre
WORKDIR /app
# Copiamos el jar generado desde la etapa de build
COPY --from=build /app/build/libs/artifact-all.jar app.jar
EXPOSE 8081
CMD ["java", "-jar", "app.jar"]