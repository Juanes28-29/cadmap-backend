package cadmap.backend

import io.ktor.server.engine.*
import io.ktor.server.netty.*
// Ktor core
import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.response.*
// Auth/JWT
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

// Manejo de errores (StatusPages)
import io.ktor.server.plugins.statuspages.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

/**
 * Punto de entrada del servidor (referenciado por application.conf)
 */
@Suppress("unused")
fun Application.module() {

    // 1) DB
    configureDatabases()

    // 2) Observabilidad / logs
    configureMonitoring()

    // 3) Serialización (JSON)
    configureSerialization()

    // 4) CORS, etc. (no dupliques CORS en otro lado)
    configureHTTP()

    // 5) Seguridad (JWT)
    configureSecurity()

    // 6) Errores controlados
    install(StatusPages) {
        exception<AuthenticationException> { call, cause ->
            call.respond(
                status = HttpStatusCode.Unauthorized,
                message = mapOf("error" to (cause.message ?: "Unauthorized"))
            )
        }
        exception<AuthorizationException> { call, cause ->
            call.respond(
                status = HttpStatusCode.Forbidden,
                message = mapOf("error" to (cause.message ?: "Forbidden"))
            )
        }
        exception<IllegalArgumentException> { call, cause ->
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = mapOf("error" to (cause.message ?: "Bad request"))
            )
        }
        exception<Throwable> { call, cause ->
            // Último recurso: evita filtrar detalles sensibles
            call.respond(
                status = HttpStatusCode.InternalServerError,
                message = mapOf("error" to "Internal server error")
            )
            // Log ya lo captura CallLogging/Monitoring.kt
        }
        status(HttpStatusCode.NotFound) { call, _ ->
            call.respond(
                status = HttpStatusCode.NotFound,
                message = mapOf("error" to "Not found")
            )
        }
    }

    // 7) Rutas
    configureRouting()
}

/**
 * Configuración de autenticación JWT
 */
fun Application.configureSecurity() {
    val secret = System.getenv("JWT_SECRET") ?: "super-secreto"          // TODO: usar env en prod
    val issuer = System.getenv("JWT_ISSUER") ?: "cadmap"
    val audience = System.getenv("JWT_AUDIENCE") ?: "cadmap_audience"

    install(Authentication) {
        jwt("auth-jwt") {
            realm = "CadMap App"
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .withIssuer(issuer)
                    .withAudience(audience)
                    .build()
            )
            validate { credential ->
                // Acepta el token si trae un email no vacío
                val email = credential.payload.getClaim("email").asString()
                if (!email.isNullOrBlank()) JWTPrincipal(credential.payload) else null
            }
            challenge { _, _ ->
                call.respond(
                    status = HttpStatusCode.Unauthorized,
                    message = mapOf("error" to "Token inválido o ausente")
                )
            }
        }
    }
}

/** Excepciones de ayuda para StatusPages */
class AuthenticationException(message: String? = null) : RuntimeException(message)
class AuthorizationException(message: String? = null) : RuntimeException(message)