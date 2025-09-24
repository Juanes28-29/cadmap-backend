package cadmap.backend.services

import cadmap.backend.database.VwCasoHistorial
import cadmap.backend.database.mappers.toVwCasoHistorialDTO
import cadmap.backend.models.VwCasoHistorialDTO
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class VwCasoHistorialService {

    fun obtenerTodos(): List<VwCasoHistorialDTO> = transaction {
        VwCasoHistorial.selectAll().map { it.toVwCasoHistorialDTO() }
    }

    fun obtenerPorCaso(casoId: UUID): List<VwCasoHistorialDTO> = transaction {
        VwCasoHistorial
            .select { VwCasoHistorial.casoId eq casoId }
            .map { it.toVwCasoHistorialDTO() }
    }
}