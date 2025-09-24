package cadmap.backend.database.mappers

import cadmap.backend.database.CatAccionCustodia
import cadmap.backend.models.CatAccionCustodiaDTO
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toCatAccionCustodiaDTO() = CatAccionCustodiaDTO(
    id = this[CatAccionCustodia.id],
    codigo = this[CatAccionCustodia.codigo],
    descripcion = this[CatAccionCustodia.descripcion]
)