package cadmap.backend.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import kotlinx.datetime.Instant
import java.util.UUID

@Serializable
data class PersonaCasoDTO(
    @Contextual val id: UUID? = null,
    @Contextual val personaId: UUID,
    @Contextual val casoId: UUID,
    val rolEnCaso: String,
    val observaciones: String? = null,
    @Contextual val createdAt: Instant? = null,
    @Contextual val updatedAt: Instant? = null
)