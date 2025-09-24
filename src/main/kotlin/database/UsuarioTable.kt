package cadmap.backend.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.UUID

object Usuarios : Table("usuarios") {
    val id = uuid("id").autoGenerate()
    val username = varchar("username", 50)
    val email = varchar("email", 100)
    val passwordHash = varchar("password_hash", 255)
    val nombre = varchar("nombre", 255)
    val apellidos = varchar("apellidos", 255)
    val rolId = integer("rol_id")
    val institucion = varchar("institucion", 255)
    val cargo = varchar("cargo", 255)
    val telefono = varchar("telefono", 255)
    val cedulaProfesional = varchar("cedula_profesional", 255).nullable()
    val activo = bool("activo")
    val ultimoAcceso = timestamp("ultimo_acceso").nullable()
    val intentosFallidos = integer("intentos_fallidos")
    val bloqueadoHasta = timestamp("bloqueado_hasta").nullable()
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")

    override val primaryKey = PrimaryKey(id)
}
