package cadmap.backend.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.util.UUID
import kotlinx.datetime.Instant
import kotlinx.serialization.json.JsonObject

@Serializable
data class NotificacionDTO(
    @Contextual val id: UUID? = null,
    @Contextual val usuarioId: UUID,
    val tipo: String,
    val titulo: String,
    val mensaje: String,
    val leida: Boolean,
    @Contextual val fechaLectura: Instant? = null,
    val metadata: JsonObject,
    @Contextual val createdAt: Instant? = null
)