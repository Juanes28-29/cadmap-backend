package cadmap.backend.services

import cadmap.backend.database.TiposIncidente
import cadmap.backend.database.mappers.toTipoIncidenteDTO
import cadmap.backend.models.TipoIncidenteDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class TipoIncidenteService {

    fun obtenerTodos(): List<TipoIncidenteDTO> = transaction {
        TiposIncidente.selectAll().map { it.toTipoIncidenteDTO() }
    }

    fun obtenerPorId(id: Int): TipoIncidenteDTO? = transaction {
        TiposIncidente.select { TiposIncidente.id eq id }
            .map { it.toTipoIncidenteDTO() }
            .singleOrNull()
    }
}