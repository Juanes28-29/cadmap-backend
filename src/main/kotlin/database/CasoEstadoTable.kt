package cadmap.backend.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.UUID

object CasoEstados : Table("caso_estados") {
    val id = uuid("id")
    val casoId = uuid("caso_id")
    val estadoAnterior = integer("estado_anterior").nullable()
    val estadoNuevo = integer("estado_nuevo")
    val motivo = text("motivo").nullable()
    val usuarioId = uuid("usuario_id")
    val fecha = timestamp("fecha")

    override val primaryKey = PrimaryKey(id)
}