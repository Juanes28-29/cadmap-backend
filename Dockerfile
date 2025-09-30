FROM openjdk:17-jdk-slim

# Establecer directorio de trabajo
WORKDIR /app

# Copiar el .jar generado por Gradle (fat jar)
COPY build/libs/artifact-all.jar app.jar

# Exponer el puerto que Render asigne dinámicamente
EXPOSE 8080

# Ejecutar la app con el puerto dinámico
CMD ["sh", "-c", "java -jar app.jar -port=${PORT}"]