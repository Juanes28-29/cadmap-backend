package cadmap.backend.services

import cadmap.backend.database.VwCasoUltimoEstado
import cadmap.backend.database.mappers.toVwCasoUltimoEstadoDTO
import cadmap.backend.models.VwCasoUltimoEstadoDTO
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.select
import java.util.UUID

class VwCasoUltimoEstadoService {

    fun obtenerTodos(): List<VwCasoUltimoEstadoDTO> = transaction {
        VwCasoUltimoEstado.selectAll().map { it.toVwCasoUltimoEstadoDTO() }
    }

    fun obtenerPorCaso(casoId: UUID): VwCasoUltimoEstadoDTO? = transaction {
        VwCasoUltimoEstado
            .select { VwCasoUltimoEstado.casoId eq casoId }
            .map { it.toVwCasoUltimoEstadoDTO() }
            .singleOrNull()
    }
}