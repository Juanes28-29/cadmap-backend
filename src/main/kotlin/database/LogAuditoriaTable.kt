package cadmap.backend.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.UUID

object LogsAuditoria : Table("logs_auditoria") {
    val id = uuid("id").autoGenerate()
    val tablaAfectada = varchar("tabla_afectada", 50)
    val registroId = varchar("registro_id", 255)
    val accion = varchar("accion", 20)
    val usuarioId = uuid("usuario_id")
    val ipAddress = varchar("ip_address", 100) // inet → String
    val userAgent = text("user_agent")
    val datosAnteriores = text("datos_anteriores") // jsonb → String
    val datosNuevos = text("datos_nuevos") // jsonb → String
    val descripcion = text("descripcion")
    val timestamp = timestamp("timestamp")
    val sessionId = varchar("session_id", 100)

    override val primaryKey = PrimaryKey(id)
}