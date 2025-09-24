package cadmap.backend.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.util.UUID
import kotlinx.datetime.Instant

@Serializable
data class MedioDTO(
    @Contextual val id: UUID? = null,
    val entidad: String,
    @Contextual val entidadId: UUID,
    val tipo: String,
    val url: String,
    val mimeType: String,
    val tamanoBytes: Long,
    val hashIntegridad: String,
    val descripcion: String,
    @Contextual val creadoPor: UUID,
    @Contextual val creadoEn: Instant? = null
)