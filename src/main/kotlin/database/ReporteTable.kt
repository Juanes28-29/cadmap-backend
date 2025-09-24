package cadmap.backend.database

import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.Table
import java.util.UUID
import cadmap.backend.database.custom.JsonbColumnType
import cadmap.backend.database.custom.jsonb

object Reportes : Table("reportes") {
    val id = uuid("id").autoGenerate()
    val nombre = varchar("nombre", 200)
    val tipoReporte = varchar("tipo_reporte", 50)
    val parametros = jsonb("parametros").nullable()
    val usuarioGeneradorId = uuid("usuario_generador_id")
    val fechaGeneracion = timestamp("fecha_generacion").nullable()
    val archivoUrl = text("archivo_url").nullable()
    val formato = varchar("formato", 10)
    val estado = varchar("estado", 20)
    val descargas = integer("descargas").default(0)
    val publico = bool("publico").default(false)
    val fechaExpiracion = timestamp("fecha_expiracion").nullable()
    val updatedAt = timestamp("updated_at").nullable()

    override val primaryKey = PrimaryKey(id)
}