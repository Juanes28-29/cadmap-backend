package cadmap.backend.services

import cadmap.backend.database.CausasMuerte
import cadmap.backend.database.mappers.toCausaMuerteDTO
import cadmap.backend.models.CausaMuerteDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class CausaMuerteService {

    fun obtenerTodos(): List<CausaMuerteDTO> = transaction {
        CausasMuerte.selectAll().map { it.toCausaMuerteDTO() }
    }

    fun obtenerPorId(id: Int): CausaMuerteDTO? = transaction {
        CausasMuerte.select { CausasMuerte.id eq id }
            .map { it.toCausaMuerteDTO() }
            .singleOrNull()
    }
}
