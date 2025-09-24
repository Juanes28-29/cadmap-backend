package cadmap.backend.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.UUID

object VistaCadenaCustodia : Table("vista_cadena_custodia") {
    val id = uuid("id")
    val evidenciaId = uuid("evidencia_id").nullable()
    val numeroEvidencia = varchar("numero_evidencia", 20).nullable()
    val incidenteId = uuid("incidente_id").nullable()
    val accionId = integer("accion_id").nullable()
    val accionCodigo = varchar("accion_codigo", 50).nullable()
    val accionDescripcion = text("accion_descripcion").nullable()
    val responsableTipo = varchar("responsable_tipo", 10).nullable()
    val responsableId = uuid("responsable_id").nullable()
    val responsableUsuario = varchar("responsable_usuario", 100).nullable()
    val responsableNombre = text("responsable_nombre").nullable()
    val fechaHora = timestamp("fecha_hora").nullable()
    val ubicacion = text("ubicacion").nullable()
    val ubicacionWkt = text("ubicacion_wkt").nullable()
    val observaciones = text("observaciones").nullable()
    val soporteUrl = text("soporte_url").nullable()
    val hashSoporte = text("hash_soporte").nullable()
    val creadoPor = uuid("creado_por").nullable()
    val creadoPorUsuario = varchar("creado_por_usuario", 100).nullable()
    val createdAt = timestamp("created_at").nullable()
}