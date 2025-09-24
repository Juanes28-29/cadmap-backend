package cadmap.backend.services

import cadmap.backend.database.EstadosDescomposicion
import cadmap.backend.database.mappers.toEstadoDescomposicionDTO
import cadmap.backend.models.EstadoDescomposicionDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class EstadoDescomposicionService {

    fun obtenerTodos(): List<EstadoDescomposicionDTO> = transaction {
        EstadosDescomposicion.selectAll().map { it.toEstadoDescomposicionDTO() }
    }

    fun obtenerPorId(id: Int): EstadoDescomposicionDTO? = transaction {
        EstadosDescomposicion
            .select { EstadosDescomposicion.id eq id }
            .map { it.toEstadoDescomposicionDTO() }
            .singleOrNull()
    }
}