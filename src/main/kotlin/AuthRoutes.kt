package cadmap.backend

import cadmap.backend.models.LoginRequest
import cadmap.backend.security.JwtConfig
import cadmap.backend.security.PasswordUtil
import cadmap.backend.database.Usuarios
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.authRoutes() {

    post("/login") {
        val login = call.receive<LoginRequest>()

        // Buscar usuario por email
        val user = transaction {
            Usuarios.select { Usuarios.email eq login.email }
                .singleOrNull()
        }

        if (user == null) {
            call.respond(HttpStatusCode.Unauthorized, "Usuario no encontrado")
            return@post
        }

        // Verificar contraseña con BCrypt
        val passwordCorrect = PasswordUtil.verifyPassword(
            login.password,
            user[Usuarios.passwordHash]
        )

        if (!passwordCorrect) {
            call.respond(HttpStatusCode.Unauthorized, "Contraseña incorrecta")
            return@post
        }

        // Mapear rol_id a string de rol
        val rolId = user[Usuarios.rolId]
        val rol = when (rolId) {
            1 -> "admin"
            2 -> "forense"
            3 -> "analista"
            else -> "invitado"
        }

        // Generar token JWT
        val token = JwtConfig.generateToken(
            email = user[Usuarios.email],
            username = user[Usuarios.username],
            rol = rol
        )

        // Devolver el token como respuesta
        call.respond(HttpStatusCode.OK, mapOf("token" to token))
    }
}