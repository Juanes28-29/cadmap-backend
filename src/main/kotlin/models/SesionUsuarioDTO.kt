package cadmap.backend.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import kotlinx.datetime.Instant
import kotlinx.serialization.json.JsonObject
import java.util.UUID

@Serializable
data class SesionUsuarioDTO(
    @Contextual val id: UUID? = null,
    @Contextual val usuarioId: UUID,
    val tokenSesion: String,
    val ipAddress: String? = null,
    val userAgent: String? = null,
    val fechaInicio: Instant? = null,
    val fechaUltimoAcceso: Instant? = null,
    val fechaExpiracion: Instant? = null,
    val activa: Boolean = true,
    val ubicacionAcceso: String? = null, // USER-DEFINED => String hasta definir PostGIS
    val dispositivoInfo: JsonObject? = null,
    val updatedAt: Instant? = null
)
