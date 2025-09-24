package cadmap.backend.database.mappers

import cadmap.backend.database.TiposIncidente
import cadmap.backend.models.TipoIncidenteDTO
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toTipoIncidenteDTO() = TipoIncidenteDTO(
    id = this[TiposIncidente.id],
    codigo = this[TiposIncidente.codigo],
    nombre = this[TiposIncidente.nombre],
    descripcion = this[TiposIncidente.descripcion],
    categoria = this[TiposIncidente.categoria],
    activo = this[TiposIncidente.activo]
)