package cadmap.backend.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.date
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.UUID

object Persona : Table("personas") {
    val id = uuid("id").autoGenerate()
    val usuarioId = uuid("usuario_id")
    val tipoPersona = varchar("tipo_persona", 20)
    val nombres = varchar("nombres", 100)
    val apellidos = varchar("apellidos", 50)
    val documento = varchar("documento", 100)
    val fechaNacimiento = date("fecha_nacimiento")
    val sexo = varchar("sexo", 10)
    val direccion = text("direccion")
    val telefono = varchar("telefono", 30)
    val email = varchar("email", 100)
    val confidencial = bool("confidencial")
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")

    override val primaryKey = PrimaryKey(id)
}