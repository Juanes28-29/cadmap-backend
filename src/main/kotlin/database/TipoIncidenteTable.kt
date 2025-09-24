package cadmap.backend.database

import org.jetbrains.exposed.sql.Table

object TiposIncidente : Table("tipos_incidente") {
    val id = integer("id").autoIncrement()
    val codigo = varchar("codigo", 10)
    val nombre = varchar("nombre", 100)
    val descripcion = text("descripcion").nullable()
    val categoria = varchar("categoria", 50).nullable()
    val activo = bool("activo").default(true)

    override val primaryKey = PrimaryKey(id)
}