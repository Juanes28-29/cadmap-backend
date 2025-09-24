package cadmap.backend.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.UUID

object VwCasoHistorial : Table("vw_caso_historial") {
    val id = uuid("id")
    val casoId = uuid("caso_id")
    val numeroCaso = varchar("numero_caso", 50)
    val fecha = timestamp("fecha")
    val motivo = text("motivo").nullable()
    val estadoAnteriorId = integer("estado_anterior_id").nullable()
    val estadoAnterior = varchar("estado_anterior", 255).nullable()
    val estadoNuevoId = integer("estado_nuevo_id").nullable()
    val estadoNuevo = varchar("estado_nuevo", 255).nullable()
    val usuarioId = uuid("usuario_id")
    val usuarioCambio = varchar("usuario_cambio", 100).nullable()
}