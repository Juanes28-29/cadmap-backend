# CadMap Backend

API REST desarrollada en Kotlin con Ktor para la gestión de información forense y de investigación, con PostgreSQL y PostGIS.

## Descripción

CadMap Backend gestiona información relacionada con casos de investigación, incidentes, evidencias, cadena de custodia, personas, laboratorios, pruebas y análisis forenses, información post mortem, usuarios, sesiones, auditoría, notificaciones, reportes, y análisis de agrupación geográfica (clustering) sobre incidentes, con control de acceso basado en roles.

## Objetivo

Centralizar la gestión de información forense y de investigación mediante una API REST que permita registrar, consultar y relacionar casos, incidentes, evidencias, personas, información de laboratorio y datos geoespaciales, manteniendo trazabilidad y control de acceso sobre la información.

Este repositorio contiene el backend/API del sistema, desarrollado en su totalidad por el autor de este repositorio.

## Responsabilidad del autor

El desarrollo completo del backend fue responsabilidad del autor, incluyendo:

- Diseño y desarrollo de la API REST (rutas, servicios, lógica de negocio).
- Modelo de datos en PostgreSQL.
- Integración con PostGIS para el componente geoespacial.
- Capa de acceso a datos con JetBrains Exposed (tablas, DTOs, mappers).
- Manejo de datos geoespaciales y GeoJSON.
- Autenticación JWT y autorización basada en roles.
- Hashing de contraseñas con BCrypt.
- Configuración del pool de conexiones con HikariCP.
- Manejo centralizado de errores con StatusPages.
- Contenerización con Docker y despliegue.

## Arquitectura

Cliente
  |
  | HTTP / REST
  v
Ktor / Netty
  |
  +-- Routing
  |
  +-- Security / JWT
  |
  +-- Services
  |
  +-- Exposed
  |
  v
PostgreSQL
  |
  +-- PostGIS

## Flujo de una petición

Cliente
  |
  v
Ktor Routing
  |
  v
Autorización por rol
  |
  v
Service
  |
  v
Exposed / Transaction
  |
  v
PostgreSQL / PostGIS
  |
  v
Mapper
  |
  v
DTO
  |
  v
JSON

## Decisiones de diseño

Backend agnóstico al algoritmo de clustering. AnalisisClustering almacena el algoritmo como un campo de texto libre (String), no como un enum fijo. Esto significa que el backend no está acoplado a una implementación concreta de clustering: puede recibir y persistir resultados generados por DBSCAN, K-Means o cualquier otro método sin cambios en el modelo de datos ni en la API.

Separación por capas. Cada recurso del dominio sigue el mismo patrón: Route -> Service -> Exposed -> PostgreSQL/PostGIS. La ruta valida el rol del usuario y delega en el servicio, el servicio contiene la lógica de negocio y usa Exposed dentro de una transacción, y Exposed traduce esas operaciones a SQL sobre PostgreSQL.

Abstracción geoespacial. Para trabajar con columnas geometry(Point, 4326) de PostGIS desde Exposed (que no tiene soporte nativo para tipos geométricos), se implementó GeometryColumnType como tipo de columna a medida. La conversión hacia y desde GeoJSON se resuelve con los serializadores GeoJSONPoint y GeoJSONFlexibleSerializer, que permiten aceptar la ubicación tanto en formato GeoJSON directo como en las variantes que devuelve PostGIS.

## Análisis de clustering

El backend está diseñado como una capa agnóstica al algoritmo de clustering. Recibe y persiste los resultados de análisis generados por otros componentes, permitiendo almacenar resultados de DBSCAN, K-Means u otros métodos sin modificar el modelo de datos ni la API.

Para esto existen las entidades AnalisisClustering, Cluster e IncidenteCluster, cada una con su DTO, su servicio CRUD y sus rutas protegidas por rol (/analisis_clustering, /clusters, /incidentes_clusters).

Entre los datos que se almacenan están: nombre del algoritmo (texto libre), parámetros y resultados estadísticos del análisis; centroide, densidad, radio, nivel de riesgo y número de incidentes por cluster; y la relación entre cada incidente y el cluster al que pertenece, incluyendo distancia y probabilidad de pertenencia al centroide.

## Información geoespacial

La ubicación de los incidentes se almacena en PostgreSQL/PostGIS como geometry(Point, 4326). Como Exposed no tiene soporte nativo para este tipo, se implementó GeometryColumnType para leerlo y escribirlo desde la capa de acceso a datos.

Para exponer y recibir esa ubicación por la API en formato GeoJSON, se usan los serializadores GeoJSONPoint y GeoJSONFlexibleSerializer (en el paquete serializers), que interpretan tanto un GeoJSON directo como el texto que devuelve PostGIS al convertir la geometría (por ejemplo mediante ST_AsGeoJSON).

## Estructura del proyecto

src/main/kotlin/
  Application.kt        Punto de entrada, configuración de seguridad JWT y StatusPages
  Routing.kt             Definición de todas las rutas de la API
  Databases.kt           Configuración de conexión a PostgreSQL con HikariCP
  HTTP.kt                 Configuración de CORS
  Monitoring.kt           Logging de peticiones (CallLogging)
  Serialization.kt        Configuración de kotlinx.serialization
  security/
    JwtConfig.kt          Generación y verificación de tokens JWT (HMAC256)
    Authorization.kt       Función authorize() para autorización por rol
    PasswordUtil.kt         Hashing y verificación de contraseñas con BCrypt
  database/
    *Table.kt              Definición de tablas y vistas con Exposed
    custom/                Tipos de columna a medida (geometry, jsonb, arrays)
    mappers/                Conversión entre filas de Exposed y DTOs
  models/
    *DTO.kt                 DTOs serializables expuestos por la API
  services/
    *Service.kt              Lógica de negocio y operaciones CRUD por entidad
  serializers/
    GeoJSONFlexibleSerializer.kt   Serialización de ubicaciones a/desde GeoJSON
    BigDecimalSerializer.kt         Serialización de valores decimales

## Base de datos

El modelo utiliza PostgreSQL con la extensión PostGIS, y está compuesto por 30 tablas y 5 vistas, definidas con JetBrains Exposed y gestionadas mediante un pool de conexiones HikariCP.

- Investigación: casos, incidentes, caso_estados, cat_estados_caso, tipos_incidente.
- Evidencia y custodia: evidencias, cadena_custodia, cat_acciones_custodia, tipos_evidencia, evidencias_envios, envios_lab.
- Forense y laboratorio: labs_forenses, pruebas_lab, analisis_forenses, informacion_cadaver, lesiones, causas_muerte, mecanismos_muerte, estados_descomposicion.
- Personas: personas, personas_casos.
- Clustering: analisis_clustering, clusters, incidentes_clusters.
- Sistema: usuarios, sesiones_usuario, logs_auditoria, notificaciones, reportes, medios.
- Vistas: vista_cadena_custodia, vista_casos_completa, vista_incidentes_cadaver, vw_caso_historial, vw_caso_ultimo_estado.

## API

La API expone los recursos del dominio con distintos niveles de acceso según su naturaleza: los recursos principales del dominio tienen operaciones completas de creación, lectura, actualización y eliminación; los catálogos de referencia y las vistas son de solo lectura.

Endpoint de autenticación:

| Recurso | Operaciones | Descripción |
|---|---|---|
| /login | POST | Autenticación de usuario y emisión de token JWT |
| /register | POST | Registro de nuevo usuario |
| /estado | GET | Verificación de que el servidor está activo |

Recursos con operaciones completas (GET, GET /{id}, POST, PUT /{id}, DELETE /{id}):

| Recurso | Descripción |
|---|---|
| /casos | Gestión de casos de investigación |
| /incidentes | Gestión de incidentes asociados a un caso |
| /evidencias | Gestión de evidencias |
| /cadenas_custodia | Registro de cadena de custodia |
| /personas | Gestión de personas relacionadas con un caso |
| /personas_casos | Relación entre personas y casos |
| /labs_forenses | Gestión de laboratorios forenses |
| /pruebas_lab | Gestión de pruebas de laboratorio |
| /envios_lab | Envíos de evidencia a laboratorio |
| /evidencias_envios | Relación entre evidencias y envíos |
| /informacion_cadaver | Información post mortem |
| /lesiones | Registro de lesiones |
| /analisis_forenses | Gestión de análisis forenses |
| /analisis_clustering | Gestión de análisis de clustering |
| /clusters | Gestión de clusters |
| /incidentes_clusters | Relación entre incidentes y clusters |
| /medios | Gestión de archivos multimedia asociados |
| /notificaciones | Gestión de notificaciones |
| /reportes | Gestión de reportes |
| /sesiones_usuario | Gestión de sesiones de usuario |

Catálogos y consultas de solo lectura (GET, GET /{id}):

| Recurso | Descripción |
|---|---|
| /tipos_evidencia | Catálogo de tipos de evidencia |
| /tipos_incidente | Catálogo de tipos de incidente |
| /cat_acciones_custodia | Catálogo de acciones de cadena de custodia |
| /cat_estados_caso | Catálogo de estados de caso |
| /caso_estados | Historial de estados de un caso (lectura y registro) |
| /causas_muerte | Catálogo de causas de muerte |
| /mecanismos_muerte | Catálogo de mecanismos de muerte |
| /estados_descomposicion | Catálogo de estados de descomposición |
| /logs_auditoria | Consulta de logs de auditoría |
| /vista_cadena_custodia | Vista consolidada de cadena de custodia |
| /vista_casos_completa | Vista consolidada de un caso |
| /vista_incidentes_cadaver | Vista consolidada de incidentes con información de cadáver |
| /vw_caso_historial | Vista de historial de un caso |
| /vw_caso_ultimo_estado | Vista del último estado de un caso |

/caso_estados es la única excepción dentro de los catálogos: además de listar, permite registrar un nuevo estado (GET y POST), pero no expone actualización ni eliminación.

Cada respuesta se serializa a JSON con kotlinx.serialization. Los errores se manejan de forma centralizada con StatusPages, devolviendo códigos HTTP coherentes (400, 401, 403, 404, 500) sin filtrar detalles internos.

## Autenticación y autorización

- POST /login valida las credenciales contra la tabla usuarios, verifica la contraseña con BCrypt y, si es correcta, emite un token JWT.
- El token se firma con el algoritmo HMAC256, usando el secreto definido en JWT_SECRET, con emisor cadmap y audiencia cadmap_audience, y una validez de una hora. Incluye el email, el nombre de usuario y el rol.
- Cada ruta protegida usa la función call.authorize(roles...) para validar el JWT recibido y comprobar que el rol del usuario esté entre los permitidos para esa operación. Por ejemplo, eliminar un análisis de clustering solo está permitido para el rol Administrador, mientras que consultarlo está permitido también para Analista.
- Si el token es inválido o no se envía, la API responde 401 (Unauthorized). Si el rol no tiene permiso sobre la operación, responde 403 (Forbidden).
- Los roles del sistema son Administrador, Forense y Analista.

## Tecnologías

- Lenguaje: Kotlin
- Framework: Ktor 2.3.7 (motor Netty)
- Base de datos: PostgreSQL (alojada en Supabase) con extensión PostGIS
- ORM / acceso a datos: JetBrains Exposed 0.50.1
- Pool de conexiones: HikariCP
- Autenticación: JWT (java-jwt / Ktor Auth JWT)
- Hashing de contraseñas: BCrypt (jbcrypt)
- Serialización: kotlinx.serialization
- Contenerización: Docker (build multietapa con Gradle + Eclipse Temurin JRE 21)

## Variables de entorno

| Variable | Descripción |
|---|---|
| DB_URL | URL JDBC de conexión a PostgreSQL |
| DB_USER | Usuario de PostgreSQL |
| DB_PASSWORD | Contraseña de PostgreSQL |
| JWT_SECRET | Secreto utilizado para firmar y verificar los JWT |
| JWT_ISSUER | Emisor esperado del JWT |
| JWT_AUDIENCE | Audiencia esperada del JWT |
| PORT | Puerto en el que se ejecuta el servidor |

Ejemplo de configuración:

DB_URL=jdbc:postgresql://<host>:5432/<db>?sslmode=require
DB_USER=<usuario>
DB_PASSWORD=<contraseña>

JWT_SECRET=<secreto>
JWT_ISSUER=cadmap
JWT_AUDIENCE=cadmap_audience

PORT=8080

## Ejecución

Ejecución local con Gradle:

./gradlew run

Ejecución con Docker:

docker build -t cadmap-backend .
docker run -p 8080:8080 --env-file .env cadmap-backend

## Estado del proyecto

El backend cuenta con el modelo de datos completo, la capa de persistencia y la API REST implementadas y funcionales para todos los recursos del dominio, incluyendo autenticación JWT, autorización por roles y manejo del componente geoespacial. El cálculo de los algoritmos de clustering (por ejemplo DBSCAN o K-Means) no forma parte de este repositorio; el backend está diseñado para almacenar y exponer sus resultados.

## Posibles líneas de evolución

- Incorporar el cálculo de clustering (DBSCAN/K-Means) como servicio propio o integrado, en lugar de depender de un componente externo.
- Añadir pruebas automatizadas más amplias sobre servicios y rutas.
- Externalizar por completo credenciales y secretos (actualmente algunos valores tienen defaults de desarrollo en el código).
- Documentar la API con OpenAPI/Swagger.

## Propósito académico

Este proyecto fue desarrollado como parte del trabajo de grado del autor en Ingeniería de Datos y Software (Universidad de San Buenaventura, Medellín).
