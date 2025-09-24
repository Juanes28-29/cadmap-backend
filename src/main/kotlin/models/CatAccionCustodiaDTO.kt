package cadmap.backend.models

import kotlinx.serialization.Serializable

@Serializable
data class CatAccionCustodiaDTO(
    val id: Int,
    val codigo: String,
    val descripcion: String? = null
)