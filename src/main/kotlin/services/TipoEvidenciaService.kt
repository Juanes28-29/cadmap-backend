package cadmap.backend.services

import cadmap.backend.database.TiposEvidencia
import cadmap.backend.database.mappers.toTipoEvidenciaDTO
import cadmap.backend.models.TipoEvidenciaDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class TipoEvidenciaService {

    fun obtenerTodos(): List<TipoEvidenciaDTO> = transaction {
        TiposEvidencia.selectAll().map { it.toTipoEvidenciaDTO() }
    }

    fun obtenerPorId(id: Int): TipoEvidenciaDTO? = transaction {
        TiposEvidencia.select { TiposEvidencia.id eq id }
            .map { it.toTipoEvidenciaDTO() }
            .singleOrNull()
    }
}
