package cadmap.backend.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.UUID

object VwCasoUltimoEstado : Table("vw_caso_ultimo_estado") {
    val casoId = uuid("caso_id")
    val numeroCaso = varchar("numero_caso", 50)
    val fechaUltimoCambio = timestamp("fecha_ultimo_cambio")
    val estadoActual = varchar("estado_actual", 100).nullable()
    val usuarioCambio = varchar("usuario_cambio", 100).nullable()
    val motivo = text("motivo").nullable()
}