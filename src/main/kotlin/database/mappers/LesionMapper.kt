package cadmap.backend.database.mappers

import cadmap.backend.database.Lesion
import cadmap.backend.models.LesionDTO
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toLesionDTO(): LesionDTO = LesionDTO(
    id = this[Lesion.id],
    cadaverId = this[Lesion.cadaverId],
    tipoLesion = this[Lesion.tipoLesion],
    ubicacionAnatomica = this[Lesion.ubicacionAnatomica],
    descripcionDetallada = this[Lesion.descripcionDetallada],
    dimensiones = this[Lesion.dimensiones],
    caracteristicasEspeciales = this[Lesion.caracteristicasEspeciales],
    patronLesional = this[Lesion.patronLesional],
    vitalOPostmortem = this[Lesion.vitalOPostmortem],
    mecanismoProduccion = this[Lesion.mecanismoProduccion],
    instrumentoProbable = this[Lesion.instrumentoProbable],
    severidad = this[Lesion.severidad],
    ordenNumeracion = this[Lesion.ordenNumeracion],
    fotografias = this[Lesion.fotografias],
    createdAt = this[Lesion.createdAt],
    updatedAt = this[Lesion.updatedAt]
)