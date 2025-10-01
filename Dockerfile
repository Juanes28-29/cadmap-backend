# ======================
# Etapa de compilación
# ======================
FROM gradle:8.5-jdk21 AS build
WORKDIR /app

# Copiar solo lo necesario para evitar cache roto
COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle ./gradle

# Descarga dependencias primero (mejora velocidad en re-deploys)
RUN ./gradlew dependencies --no-daemon || return 0

# Copiar el resto del código
COPY . .

# Compilar directamente el shadowJar (sin tests para que no se frene el build)
RUN ./gradlew clean shadowJar --no-daemon -x test

# ======================
# Etapa de ejecución
# ======================
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copiamos el jar generado desde la etapa de build
COPY --from=build /app/build/libs/artifact-all.jar app.jar

# Exponer el puerto dinámico para Render/Railway
EXPOSE 8080

# Usar PORT dinámico (Render/Railway lo inyecta en $PORT)
CMD ["sh", "-c", "java -jar app.jar --server.port=${PORT:-8080}"]