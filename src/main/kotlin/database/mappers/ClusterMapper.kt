package cadmap.backend.database.mappers

import cadmap.backend.database.Clusters
import cadmap.backend.models.ClusterDTO
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toClusterDTO() = ClusterDTO(
    id = this[Clusters.id].value,
    analisisId = this[Clusters.analisisId],
    numeroCluster = this[Clusters.numeroCluster],
    centroide = this[Clusters.centroide],
    areaInfluencia = this[Clusters.areaInfluencia],
    numeroIncidentes = this[Clusters.numeroIncidentes],
    densidad = this[Clusters.densidad],
    radioMetros = this[Clusters.radioMetros],
    nivelRiesgo = this[Clusters.nivelRiesgo],
    descripcion = this[Clusters.descripcion],
    recomendaciones = this[Clusters.recomendaciones],
    colorVisualizacion = this[Clusters.colorVisualizacion],
    activo = this[Clusters.activo],
    createdAt = this[Clusters.createdAt]
)