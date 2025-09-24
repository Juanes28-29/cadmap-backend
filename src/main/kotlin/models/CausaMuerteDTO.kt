package cadmap.backend.models

import kotlinx.serialization.Serializable

@Serializable
data class CausaMuerteDTO(
    val id: Int,
    val codigoCie10: String? = null,
    val descripcion: String? = null,
    val categoria: String? = null,
    val activo: Boolean
)
