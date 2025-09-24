package cadmap.backend.database

import org.jetbrains.exposed.sql.Table

object CatAccionCustodia : Table("cat_acciones_custodia") {
    val id = integer("id").autoIncrement()
    val codigo = varchar("codigo", 30)
    val descripcion = text("descripcion").nullable()

    override val primaryKey = PrimaryKey(id)
}