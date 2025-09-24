package cadmap.backend.services

import cadmap.backend.database.LogsAuditoria
import cadmap.backend.database.mappers.toLogAuditoriaDTO
import cadmap.backend.models.LogAuditoriaDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class LogAuditoriaService {

    fun obtenerTodos(): List<LogAuditoriaDTO> = transaction {
        LogsAuditoria.selectAll().map { it.toLogAuditoriaDTO() }
    }

    fun obtenerPorId(id: UUID): LogAuditoriaDTO? = transaction {
        LogsAuditoria
            .select { LogsAuditoria.id eq id }
            .map { it.toLogAuditoriaDTO() }
            .singleOrNull()
    }
}