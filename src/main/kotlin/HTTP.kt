package cadmap.backend

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureHTTP() {
    // Evita doble instalación
    if (pluginOrNull(CORS) == null) {
        install(CORS) {
            allowMethod(HttpMethod.Options)
            allowMethod(HttpMethod.Put)
            allowMethod(HttpMethod.Delete)
            allowMethod(HttpMethod.Patch)

            allowHeader(HttpHeaders.Authorization)
            allowHeader(HttpHeaders.ContentType)
            // quita "MyCustomHeader" si no lo usas
            // allowHeader("MyCustomHeader")

            anyHost() // en producción especifica hosts
            allowCredentials = true
            allowNonSimpleContentTypes = true
        }
    }
}