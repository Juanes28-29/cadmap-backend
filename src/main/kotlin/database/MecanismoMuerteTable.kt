package cadmap.backend.database

import org.jetbrains.exposed.sql.Table

object MecanismosMuerte : Table("mecanismos_muerte") {
    val id = integer("id")
    val nombre = varchar("nombre", 100)
    val descripcion = text("descripcion")
    val categoria = varchar("categoria", 50)
    val activo = bool("activo")

    override val primaryKey = PrimaryKey(id)
}