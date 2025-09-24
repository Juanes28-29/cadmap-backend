package cadmap.backend.database.mappers

import cadmap.backend.database.CadenaCustodia
import cadmap.backend.models.CadenaCustodiaDTO
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toCadenaCustodiaDTO() = CadenaCustodiaDTO(
    id = this[CadenaCustodia.id],
    evidenciaId = this[CadenaCustodia.evidenciaId],
    responsableTipo = this[CadenaCustodia.responsableTipo],
    responsableNombre = this[CadenaCustodia.responsableNombre],
    fechaHora = this[CadenaCustodia.fechaHora],
    ubicacion = this[CadenaCustodia.ubicacion],
    observaciones = this[CadenaCustodia.observaciones],
    soporteUrl = this[CadenaCustodia.soporteUrl],
    hashSoporte = this[CadenaCustodia.hashSoporte],
    creadoPor = this[CadenaCustodia.creadoPor],
    createdAt = this[CadenaCustodia.createdAt],
    accionId = this[CadenaCustodia.accionId],
    ubicacionGeom = this[CadenaCustodia.ubicacionGeom]
)