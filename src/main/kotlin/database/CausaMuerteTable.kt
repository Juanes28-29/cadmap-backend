package cadmap.backend.database

import org.jetbrains.exposed.sql.Table

object CausasMuerte : Table("causas_muerte") {
    val id = integer("id")
    val codigoCie10 = varchar("codigo_cie10", 10).nullable()
    val descripcion = text("descripcion").nullable()
    val categoria = varchar("categoria", 100).nullable()
    val activo = bool("activo")

    override val primaryKey = PrimaryKey(id)
}
