package cadmap.backend.models

import kotlinx.serialization.Serializable

@Serializable
data class MecanismoMuerteDTO(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val categoria: String,
    val activo: Boolean
)