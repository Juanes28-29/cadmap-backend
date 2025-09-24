package cadmap.backend.database.mappers

import cadmap.backend.database.VistaIncidentesCadaver
import cadmap.backend.models.VistaIncidentesCadaverDTO
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toVistaIncidenteCadaverDTO() = VistaIncidentesCadaverDTO(
    incidenteId = this[VistaIncidentesCadaver.incidenteId],
    folioMinisterial = this[VistaIncidentesCadaver.folioMinisterial],
    fechaHallazgo = this[VistaIncidentesCadaver.fechaHallazgo],
    ubicacion = this[VistaIncidentesCadaver.ubicacion],
    direccionExacta = this[VistaIncidentesCadaver.direccionExacta],
    sexo = this[VistaIncidentesCadaver.sexo],
    edadEstimadaMin = this[VistaIncidentesCadaver.edadEstimadaMin],
    edadEstimadaMax = this[VistaIncidentesCadaver.edadEstimadaMax],
    estadoDescomposicion = this[VistaIncidentesCadaver.estadoDescomposicion],
    causaMuerteId = this[VistaIncidentesCadaver.causaMuerteId],
    maneraMuerte = this[VistaIncidentesCadaver.maneraMuerte],
    numeroCaso = this[VistaIncidentesCadaver.numeroCaso]
)