package cadmap.backend.services

import cadmap.backend.database.VistaCadenaCustodia
import cadmap.backend.database.mappers.toVistaCadenaCustodiaDTO
import cadmap.backend.models.VistaCadenaCustodiaDTO
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class VistaCadenaCustodiaService {

    fun obtenerTodos(): List<VistaCadenaCustodiaDTO> = transaction {
        VistaCadenaCustodia.selectAll().map { it.toVistaCadenaCustodiaDTO() }
    }

    fun obtenerPorId(id: UUID): VistaCadenaCustodiaDTO? = transaction {
        VistaCadenaCustodia
            .select { VistaCadenaCustodia.id eq id }
            .map { it.toVistaCadenaCustodiaDTO() }
            .singleOrNull()
    }
}