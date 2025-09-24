package cadmap.backend.database.mappers

import cadmap.backend.database.IncidenteCluster
import cadmap.backend.models.IncidenteClusterDTO
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toIncidenteClusterDTO() = IncidenteClusterDTO(
    id = this[IncidenteCluster.id].value,
    incidenteId = this[IncidenteCluster.incidenteId],
    clusterId = this[IncidenteCluster.clusterId],
    distanciaCentroide = this[IncidenteCluster.distanciaCentroide],
    probabilidadPertenencia = this[IncidenteCluster.probabilidadPertenencia],
    createdAt = this[IncidenteCluster.createdAt]
)