package cadmap.backend.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import kotlinx.datetime.Instant
import java.util.UUID

@Serializable
data class LogAuditoriaDTO(
    @Contextual val id: UUID,
    val tablaAfectada: String,
    val registroId: String,
    val accion: String,
    @Contextual val usuarioId: UUID,
    val ipAddress: String,
    val userAgent: String,
    val datosAnteriores: String,
    val datosNuevos: String,
    val descripcion: String,
    val timestamp: Instant,
    val sessionId: String
)