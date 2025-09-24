package cadmap.backend.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestamp
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.UUID

object CadenaCustodia : Table("cadena_custodia") {
    val id = uuid("id").autoGenerate()
    val evidenciaId = uuid("evidencia_id")
    val responsableTipo = varchar("responsable_tipo", 10)
    val responsableNombre = text("responsable_nombre")
    val fechaHora = timestamp("fecha_hora")
    val ubicacion = text("ubicacion")
    val observaciones = text("observaciones")
    val soporteUrl = text("soporte_url")
    val hashSoporte = text("hash_soporte")
    val creadoPor = uuid("creado_por").nullable()
    val createdAt = timestamp("created_at")
    val accionId = integer("accion_id")
    val ubicacionGeom = text("ubicacion_geom") // luego lo cambiamos a geometry(PostGIS)

    override val primaryKey = PrimaryKey(id)
}