package cadmap.backend.security

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.auth.*

object Roles {
    const val ADMIN = "1"
    const val FORENSE = "2"
    const val ANALISTA = "3"
}

suspend fun ApplicationCall.authorize(vararg roles: String, block: suspend ApplicationCall.(principal: JWTPrincipal) -> Unit) {
    val principal = this.principal<JWTPrincipal>()
    if (principal == null) {
        respond(HttpStatusCode.Unauthorized, "Token inválido o ausente")
        return
    }

    val rol = principal.getClaim("rol", String::class)
    if (rol == null || rol !in roles) {
        respond(HttpStatusCode.Forbidden, "No tienes permiso para acceder a este recurso")
        return
    }
    this.block(principal)
}