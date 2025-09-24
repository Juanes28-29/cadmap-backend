package cadmap.backend.models

import kotlinx.serialization.Serializable
import java.util.UUID
import kotlinx.serialization.Contextual

@Serializable
data class LabForenseDTO(
    @Contextual val id: UUID? = null,
    val nombre: String,
    val tipo: String,
    val institucion: String,
    val direccion: String,
    val ciudad: String,
    val telefono: String,
    val email: String,
    val creadoEn: String? = null,
    @Contextual val creadoPor: UUID? = null
)