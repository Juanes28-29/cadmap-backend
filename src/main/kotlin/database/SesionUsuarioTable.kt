package cadmap.backend.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.UUID
import cadmap.backend.database.custom.JsonbColumnType
import cadmap.backend.database.custom.jsonb

object SesionesUsuario : Table("sesiones_usuario") {
    val id = uuid("id").autoGenerate()
    val usuarioId = uuid("usuario_id")
    val tokenSesion = varchar("token_sesion", 255)
    val ipAddress = varchar("ip_address", 100).nullable() // inet -> String
    val userAgent = text("user_agent").nullable()
    val fechaInicio = timestamp("fecha_inicio").nullable()
    val fechaUltimoAcceso = timestamp("fecha_ultimo_acceso").nullable()
    val fechaExpiracion = timestamp("fecha_expiracion").nullable()
    val activa = bool("activa").default(true)
    val ubicacionAcceso = text("ubicacion_acceso").nullable() // USER-DEFINED -> text
    val dispositivoInfo = jsonb("dispositivo_info").nullable()
    val updatedAt = timestamp("updated_at").nullable()

    override val primaryKey = PrimaryKey(id)
}
