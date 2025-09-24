package cadmap.backend.database

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object Clusters : UUIDTable("clusters") {
    val analisisId = uuid("analisis_id")
    val numeroCluster = integer("numero_cluster")
    val centroide = text("centroide").nullable()          // USER-DEFINED -> text temporal
    val areaInfluencia = text("area_influencia").nullable()
    val numeroIncidentes = integer("numero_incidentes")
    val densidad = decimal("densidad", precision = 10, scale = 6)
    val radioMetros = decimal("radio_metros", precision = 10, scale = 2)
    val nivelRiesgo = varchar("nivel_riesgo", 20)
    val descripcion = text("descripcion").nullable()
    val recomendaciones = text("recomendaciones").nullable()
    val colorVisualizacion = varchar("color_visualizacion", 7).nullable()
    val activo = bool("activo").default(true)
    val createdAt = timestamp("created_at").nullable()
}