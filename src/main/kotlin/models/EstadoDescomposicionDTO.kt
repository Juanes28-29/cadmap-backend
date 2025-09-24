package cadmap.backend.models

import kotlinx.serialization.Serializable

@Serializable
data class EstadoDescomposicionDTO(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val ordenSecuencial: Int,
    val activo: Boolean
)