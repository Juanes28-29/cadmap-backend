package cadmap.backend.database.mappers

import cadmap.backend.database.EstadosDescomposicion
import cadmap.backend.models.EstadoDescomposicionDTO
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toEstadoDescomposicionDTO() = EstadoDescomposicionDTO(
    id = this[EstadosDescomposicion.id],
    nombre = this[EstadosDescomposicion.nombre],
    descripcion = this[EstadosDescomposicion.descripcion],
    ordenSecuencial = this[EstadosDescomposicion.ordenSecuencial],
    activo = this[EstadosDescomposicion.activo]
)