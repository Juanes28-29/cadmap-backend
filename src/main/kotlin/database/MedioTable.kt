package cadmap.backend.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.UUID

object Medios : Table("medios") {
    val id = uuid("id").autoGenerate()
    val entidad = varchar("entidad", 40)
    val entidadId = uuid("entidad_id")
    val tipo = varchar("tipo", 20)
    val url = text("url")
    val mimeType = varchar("mime_type", 120)
    val tamanoBytes = long("tamano_bytes")
    val hashIntegridad = text("hash_integridad")
    val descripcion = text("descripcion")
    val creadoPor = uuid("creado_por")
    val creadoEn = timestamp("creado_en")

    override val primaryKey = PrimaryKey(id)
}