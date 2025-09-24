package cadmap.backend.services

import cadmap.backend.database.VistaCasosCompleta
import cadmap.backend.database.mappers.toVistaCasosCompletaDTO
import cadmap.backend.models.VistaCasosCompletaDTO
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class VistaCasosCompletaService {

    fun obtenerTodos(): List<VistaCasosCompletaDTO> = transaction {
        VistaCasosCompleta
            .selectAll()
            .map { it.toVistaCasosCompletaDTO() }
    }

    fun obtenerPorId(id: java.util.UUID): VistaCasosCompletaDTO? = transaction {
        VistaCasosCompleta
            .select { VistaCasosCompleta.id eq id }
            .map { it.toVistaCasosCompletaDTO() }
            .singleOrNull()
    }
}