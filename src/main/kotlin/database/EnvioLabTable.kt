package cadmap.backend.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.*

object EnvioLab : Table("envios_lab") {
    val id = uuid("id").autoGenerate().uniqueIndex()
    val labId = uuid("lab_id").nullable()
    val remitenteId = uuid("remitente_id").nullable()
    val fechaEnvio = timestamp("fecha_envio").nullable()
    val fechaRecepcion = timestamp("fecha_recepcion").nullable()
    val recibidoPor = text("recibido_por").nullable()
    val numeroGuia = varchar("numero_guia", 60).nullable()
    val estado = varchar("estado", 20).nullable()
    val observaciones = text("observaciones").nullable()

    override val primaryKey = PrimaryKey(id)
}
