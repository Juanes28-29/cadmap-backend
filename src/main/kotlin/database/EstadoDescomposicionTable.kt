package cadmap.backend.database

import org.jetbrains.exposed.sql.Table

object EstadosDescomposicion : Table("estados_descomposicion") {
    val id = integer("id")
    val nombre = varchar("nombre", 50)
    val descripcion = text("descripcion")
    val ordenSecuencial = integer("orden_secuencial")
    val activo = bool("activo")

    override val primaryKey = PrimaryKey(id)
}