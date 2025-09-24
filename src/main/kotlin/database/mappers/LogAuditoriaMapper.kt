package cadmap.backend.database.mappers

import cadmap.backend.database.LogsAuditoria
import cadmap.backend.models.LogAuditoriaDTO
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toLogAuditoriaDTO() = LogAuditoriaDTO(
    id = this[LogsAuditoria.id],
    tablaAfectada = this[LogsAuditoria.tablaAfectada],
    registroId = this[LogsAuditoria.registroId],
    accion = this[LogsAuditoria.accion],
    usuarioId = this[LogsAuditoria.usuarioId],
    ipAddress = this[LogsAuditoria.ipAddress],
    userAgent = this[LogsAuditoria.userAgent],
    datosAnteriores = this[LogsAuditoria.datosAnteriores],
    datosNuevos = this[LogsAuditoria.datosNuevos],
    descripcion = this[LogsAuditoria.descripcion],
    timestamp = this[LogsAuditoria.timestamp],
    sessionId = this[LogsAuditoria.sessionId]
)