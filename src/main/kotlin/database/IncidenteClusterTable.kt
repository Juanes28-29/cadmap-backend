package cadmap.backend.database

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object IncidenteCluster : UUIDTable("incidentes_clusters") {
    val incidenteId = uuid("incidente_id")
    val clusterId = uuid("cluster_id")
    val distanciaCentroide = decimal("distancia_centroide", 10, 2).nullable()
    val probabilidadPertenencia = decimal("probabilidad_pertenencia", 5, 4).nullable()
    val createdAt = timestamp("created_at").nullable()
}