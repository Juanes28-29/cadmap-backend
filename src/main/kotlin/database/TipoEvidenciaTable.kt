package cadmap.backend.database

import org.jetbrains.exposed.sql.Table

object TiposEvidencia : Table("tipos_evidencia") {
    val id = integer("id").autoIncrement()
    val nombre = varchar("nombre", 100)
    val categoria = varchar("categoria", 50).nullable()
    val descripcion = text("descripcion").nullable()
    val requiereCadenaCustodia = bool("requiere_cadena_custodia").default(false)
    val activo = bool("activo").default(true)

    override val primaryKey = PrimaryKey(id)
}
