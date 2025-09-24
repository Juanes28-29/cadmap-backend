package cadmap.backend.database

import cadmap.backend.database.custom.JsonbColumnType
import cadmap.backend.database.custom.jsonb
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.UUID

object Notificaciones : Table("notificaciones") {
    val id = uuid("id").autoGenerate()
    val usuarioId = uuid("usuario_id")
    val tipo = varchar("tipo", 50)
    val titulo = varchar("titulo", 200)
    val mensaje = text("mensaje")
    val leida = bool("leida")
    val fechaLectura = timestamp("fecha_lectura").nullable()
    val metadata = jsonb("metadata")
    val createdAt = timestamp("created_at")

    override val primaryKey = PrimaryKey(id)
}