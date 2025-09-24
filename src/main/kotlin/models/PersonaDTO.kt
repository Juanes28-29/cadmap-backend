package cadmap.backend.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.util.UUID
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

@Serializable
data class PersonaDTO(
    @Contextual val id: UUID? = null,
    @Contextual val usuarioId: UUID,
    val tipoPersona: String,
    val nombres: String,
    val apellidos: String,
    val documento: String,
    val fechaNacimiento: LocalDate,
    val sexo: String,
    val direccion: String,
    val telefono: String,
    val email: String,
    val confidencial: Boolean,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null
)