package cadmap.backend.models

import kotlinx.serialization.Serializable

@Serializable
data class TipoEvidenciaDTO(
    val id: Int,
    val nombre: String,
    val categoria: String? = null,
    val descripcion: String? = null,
    val requiereCadenaCustodia: Boolean = false,
    val activo: Boolean = true
)
