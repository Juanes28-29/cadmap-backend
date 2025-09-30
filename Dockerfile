# Imagen base de Java 17
FROM openjdk:17-jdk-slim

# Carpeta de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el JAR generado por Gradle (fat jar)
COPY build/libs/artifact-all.jar app.jar

# Puerto que usará la app (Render asigna dinámicamente la variable $PORT)
EXPOSE 8081

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]