package cadmap.backend.services

import cadmap.backend.database.CatAccionCustodia
import cadmap.backend.database.mappers.toCatAccionCustodiaDTO
import cadmap.backend.models.CatAccionCustodiaDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class CatAccionCustodiaService {

    fun obtenerTodos(): List<CatAccionCustodiaDTO> = transaction {
        CatAccionCustodia.selectAll().map { it.toCatAccionCustodiaDTO() }
    }

    fun obtenerPorId(id: Int): CatAccionCustodiaDTO? = transaction {
        CatAccionCustodia.select { CatAccionCustodia.id eq id }
            .map { it.toCatAccionCustodiaDTO() }
            .singleOrNull()
    }
}