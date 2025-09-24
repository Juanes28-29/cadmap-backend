package cadmap.backend.database.mappers

import cadmap.backend.database.CatEstadoCaso
import cadmap.backend.models.CatEstadoCasoDTO
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toCatEstadoCasoDTO() = CatEstadoCasoDTO(
    id = this[CatEstadoCaso.id],
    nombre = this[CatEstadoCaso.nombre],
    descripcion = this[CatEstadoCaso.descripcion]
)