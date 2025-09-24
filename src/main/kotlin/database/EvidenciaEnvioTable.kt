package cadmap.backend.database

import org.jetbrains.exposed.dao.id.UUIDTable

object EvidenciasEnvios : UUIDTable("evidencias_envios") {
    val envioId = uuid("envio_id")
    val evidenciaId = uuid("evidencia_id")
    val condicionSello = varchar("condicion_sello", 30)
    val numSello = varchar("num_sello", 60)
    val observaciones = text("observaciones").nullable()
}