package cadmap.backend.database

import org.jetbrains.exposed.sql.Table

object CatEstadoCaso : Table("cat_estados_caso") {
    val id = integer("id").autoIncrement()
    val nombre = varchar("nombre", 50)
    val descripcion = text("descripcion").nullable()

    override val primaryKey = PrimaryKey(id)
}