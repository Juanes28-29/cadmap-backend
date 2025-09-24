package cadmap.backend.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import kotlinx.datetime.Instant
import kotlinx.serialization.json.JsonObject
import java.util.UUID

@Serializable
data class ReporteDTO(
    @Contextual val id: UUID? = null,
    val nombre: String,
    val tipoReporte: String,
    val parametros: JsonObject? = null,
    @Contextual val usuarioGeneradorId: UUID,
    val fechaGeneracion: Instant? = null,
    val archivoUrl: String? = null,
    val formato: String,
    val estado: String,
    val descargas: Int = 0,
    val publico: Boolean = false,
    val fechaExpiracion: Instant? = null,
    val updatedAt: Instant? = null
)