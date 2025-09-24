package cadmap.backend.models

import kotlinx.serialization.Serializable

@Serializable
data class CatEstadoCasoDTO(
    val id: Int,
    val nombre: String,
    val descripcion: String? = null
)