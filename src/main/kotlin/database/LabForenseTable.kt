package cadmap.backend.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object LabForense : Table("labs_forenses") {
    val id = uuid("id").autoGenerate()
    val nombre = varchar("nombre", 150)
    val tipo = varchar("tipo", 50)
    val institucion = varchar("institucion", 150)
    val direccion = text("direccion")
    val ciudad = varchar("ciudad", 100)
    val telefono = varchar("telefono", 50)
    val email = varchar("email", 120)
    val creadoEn = timestamp("creado_en")
    val creadoPor = uuid("creado_por").references(Usuarios.id)

    override val primaryKey = PrimaryKey(id)
}