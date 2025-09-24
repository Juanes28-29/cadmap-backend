package cadmap.backend.services

import cadmap.backend.database.VistaIncidentesCadaver
import cadmap.backend.database.mappers.toVistaIncidenteCadaverDTO
import cadmap.backend.models.VistaIncidentesCadaverDTO
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class VistaIncidentesCadaverService {

    fun obtenerTodos(): List<VistaIncidentesCadaverDTO> = transaction {
        VistaIncidentesCadaver.selectAll().map { it.toVistaIncidenteCadaverDTO() }
    }

    fun obtenerPorId(id: UUID): VistaIncidentesCadaverDTO? = transaction {
        VistaIncidentesCadaver
            .select { VistaIncidentesCadaver.incidenteId eq id }
            .map { it.toVistaIncidenteCadaverDTO() }
            .singleOrNull()
    }
}