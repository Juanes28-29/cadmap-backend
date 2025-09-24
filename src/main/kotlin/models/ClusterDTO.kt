package cadmap.backend.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.util.UUID
import kotlinx.datetime.Instant
import java.math.BigDecimal

@Serializable
data class ClusterDTO(
    @Contextual val id: UUID? = null,
    @Contextual val analisisId: UUID,
    val numeroCluster: Int,
    val centroide: String? = null,           // temporal: en PostGIS se puede ajustar
    val areaInfluencia: String? = null,      // temporal
    val numeroIncidentes: Int,
    @Contextual val densidad: BigDecimal,
    @Contextual val radioMetros: BigDecimal,
    val nivelRiesgo: String,
    val descripcion: String? = null,
    val recomendaciones: String? = null,
    val colorVisualizacion: String? = null,
    val activo: Boolean = true,
    @Contextual val createdAt: Instant? = null
)