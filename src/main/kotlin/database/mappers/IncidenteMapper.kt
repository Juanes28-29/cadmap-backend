package cadmap.backend.database.mappers

import cadmap.backend.database.Incidentes
import cadmap.backend.models.IncidenteDTO
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toIncidenteDTO(): IncidenteDTO = IncidenteDTO(
    id = this[Incidentes.id],
    casoId = this[Incidentes.casoId],
    folioMinisterial = this[Incidentes.folioMinisterial],
    fechaHallazgo = this[Incidentes.fechaHallazgo],
    fechaLevantamiento = this[Incidentes.fechaLevantamiento],
    horaEstimadaMuerte = this[Incidentes.horaEstimadaMuerte],
    ubicacion = this[Incidentes.ubicacion],
    direccionExacta = this[Incidentes.direccionExacta],
    descripcionUbicacion = this[Incidentes.descripcionUbicacion],
    accesoVehicular = this[Incidentes.accesoVehicular],
    tipoLugar = this[Incidentes.tipoLugar],
    descripcionEscena = this[Incidentes.descripcionEscena],
    condicionesClimaticas = this[Incidentes.condicionesClimaticas],
    fotografiasEscena = this[Incidentes.fotografiasEscena].toList(),
    croquisUrl = this[Incidentes.croquisUrl],
    investigadorCargoId = this[Incidentes.investigadorCargoId],
    mpCargo = this[Incidentes.mpCargo],
    peritoCargo = this[Incidentes.peritoCargo],
    createdAt = this[Incidentes.createdAt],
    updatedAt = this[Incidentes.updatedAt]
)