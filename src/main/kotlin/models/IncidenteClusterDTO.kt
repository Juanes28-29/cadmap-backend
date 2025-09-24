package cadmap.backend.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import kotlinx.datetime.Instant
import java.util.UUID
import java.math.BigDecimal

@Serializable
data class IncidenteClusterDTO(
    @Contextual val id: UUID? = null,
    @Contextual val incidenteId: UUID,
    @Contextual val clusterId: UUID,
    @Contextual val distanciaCentroide: BigDecimal? = null,
    @Contextual val probabilidadPertenencia: BigDecimal? = null,
    @Contextual val createdAt: Instant? = null
)