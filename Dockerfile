# Etapa de compilación
FROM gradle:8.5-jdk21 AS build
WORKDIR /app
COPY . .
# Solo genera el shadowJar, sin ejecutar tests
RUN ./gradlew clean shadowJar -x test

# Etapa de ejecución
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/artifact-all.jar app.jar
EXPOSE 8081
CMD ["java", "-jar", "app.jar"]