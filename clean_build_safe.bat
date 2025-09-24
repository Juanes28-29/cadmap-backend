@echo off
echo =====================================
echo   🚀 Limpiando y recompilando CadMap
echo =====================================

REM Mostrar ruta actual
echo Proyecto en: %cd%

REM 0. Matar procesos que bloquean archivos (gradle, java, kotlinc)
echo [0/4] Cerrando procesos de Gradle/Java...
taskkill /F /IM gradle* 2>nul
taskkill /F /IM java.exe 2>nul
taskkill /F /IM kotlinc* 2>nul

REM Cambiar a la carpeta artifact
cd artifact

REM 1. Detener Gradle Daemon
echo [1/4] Deteniendo Gradle Daemon...
call gradlew --stop

REM 2. Borrar carpeta build
echo [2/4] Eliminando carpeta build...
rmdir /s /q "build"

REM 3. Ejecutar build limpio
echo [3/4] Compilando proyecto...
call gradlew clean build

REM Volver a la carpeta original
cd ..

echo =====================================
echo   ✅ Proceso completado
echo =====================================
pause