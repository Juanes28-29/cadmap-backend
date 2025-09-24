package cadmap.backend.models

import kotlinx.serialization.Serializable

@Serializable
data class TipoIncidenteDTO(
    val id: Int,
    val codigo: String,
    val nombre: String,
    val descripcion: String? = null,
    val categoria: String? = null,
    val activo: Boolean = true
)