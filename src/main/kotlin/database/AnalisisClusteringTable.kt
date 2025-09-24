package cadmap.backend.database

import org.jetbrains.exposed.sql.Table
import cadmap.backend.database.custom.JsonbColumnType
import cadmap.backend.database.custom.jsonb
import kotlinx.serialization.json.JsonObject
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.UUID

object AnalisisClustering : Table("analisis_clustering") {
    val id = uuid("id")
    val nombre = varchar("nombre", 100)
    val descripcion = text("descripcion").nullable()
    val algoritmo = varchar("algoritmo", 50)
    val parametros = jsonb("parametros").nullable()
    val fechaAnalisis = timestamp("fecha_analisis").nullable()
    val analistaId = uuid("analista_id")
    val areaAnalisis = text("area_analisis").nullable() // simplificado en texto
    val periodoInicio = timestamp("periodo_inicio").nullable()
    val periodoFin = timestamp("periodo_fin").nullable()
    val filtrosAplicados = jsonb("filtros_aplicados").nullable()
    val resultadosEstadisticos = jsonb("resultados_estadisticos").nullable()
    val activo = bool("activo")
    val createdAt = timestamp("created_at")

    override val primaryKey = PrimaryKey(id)
}
