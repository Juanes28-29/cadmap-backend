package cadmap.backend.database

import org.jetbrains.exposed.sql.Table
import cadmap.backend.database.custom.JsonbColumnType
import cadmap.backend.database.custom.StringArrayColumnType
import cadmap.backend.database.custom.jsonb
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.UUID

object AnalisisForenses : Table("analisis_forenses") {
    val id = uuid("id")
    val casoId = uuid("caso_id")
    val tipoAnalisis = varchar("tipo_analisis", 50)
    val numeroDictamen = varchar("numero_dictamen", 50).nullable()
    val fechaAnalisis = timestamp("fecha_analisis").nullable()
    val peritoResponsable = varchar("perito_responsable", 100).nullable()
    val laboratorio = varchar("laboratorio", 100).nullable()
    val causaMuerteId = integer("causa_muerte_id").nullable()
    val mecanismoMuerteId = integer("mecanismo_muerte_id").nullable()
    val maneraMuerte = varchar("manera_muerte", 50).nullable()
    val hallazgosPrincipales = text("hallazgos_principales").nullable()
    val hallazgosMicroscopicos = text("hallazgos_microscopicos").nullable()
    val resultadosToxicologicos = jsonb(
        "resultados_toxicologicos")
    val resultadosGeneticos = jsonb(
        "resultados_geneticos")
    val conclusiones = text("conclusiones").nullable()
    val recomendaciones = text("recomendaciones").nullable()
    val archivoDictamenUrl = text("archivo_dictamen_url").nullable()
    val fotografiasNecropsia = registerColumn<List<String>>(
        "fotografias_necropsia",
        StringArrayColumnType())
    val createdAt = timestamp("created_at").nullable()
    val updatedAt = timestamp("updated_at").nullable()

    override val primaryKey = PrimaryKey(id)
}
