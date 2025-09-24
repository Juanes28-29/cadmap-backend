package cadmap.backend.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import kotlinx.datetime.Instant
import java.util.*

@Serializable
data class EnvioLabDTO(
    @Contextual val id: UUID? = null,
    @Contextual val labId: UUID? = null,
    @Contextual val remitenteId: UUID? = null,
    val fechaEnvio: Instant? = null,
    val fechaRecepcion: Instant? = null,
    val recibidoPor: String? = null,
    val numeroGuia: String? = null,
    val estado: String? = null,
    val observaciones: String? = null
)
