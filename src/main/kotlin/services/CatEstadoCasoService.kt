package cadmap.backend.services

import cadmap.backend.database.CatEstadoCaso
import cadmap.backend.database.mappers.toCatEstadoCasoDTO
import cadmap.backend.models.CatEstadoCasoDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class CatEstadoCasoService {

    fun obtenerTodos(): List<CatEstadoCasoDTO> = transaction {
        CatEstadoCaso.selectAll().map { it.toCatEstadoCasoDTO() }
    }

    fun obtenerPorId(id: Int): CatEstadoCasoDTO? = transaction {
        CatEstadoCaso.select { CatEstadoCaso.id eq id }
            .map { it.toCatEstadoCasoDTO() }
            .singleOrNull()
    }
}