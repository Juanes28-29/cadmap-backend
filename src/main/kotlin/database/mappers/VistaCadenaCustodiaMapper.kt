package cadmap.backend.database.mappers

import cadmap.backend.database.VistaCadenaCustodia
import cadmap.backend.models.VistaCadenaCustodiaDTO
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toVistaCadenaCustodiaDTO() = VistaCadenaCustodiaDTO(
    id = this[VistaCadenaCustodia.id],
    evidenciaId = this[VistaCadenaCustodia.evidenciaId],
    numeroEvidencia = this[VistaCadenaCustodia.numeroEvidencia],
    incidenteId = this[VistaCadenaCustodia.incidenteId],
    accionId = this[VistaCadenaCustodia.accionId],
    accionCodigo = this[VistaCadenaCustodia.accionCodigo],
    accionDescripcion = this[VistaCadenaCustodia.accionDescripcion],
    responsableTipo = this[VistaCadenaCustodia.responsableTipo],
    responsableId = this[VistaCadenaCustodia.responsableId],
    responsableUsuario = this[VistaCadenaCustodia.responsableUsuario],
    responsableNombre = this[VistaCadenaCustodia.responsableNombre],
    fechaHora = this[VistaCadenaCustodia.fechaHora],
    ubicacion = this[VistaCadenaCustodia.ubicacion],
    ubicacionWkt = this[VistaCadenaCustodia.ubicacionWkt],
    observaciones = this[VistaCadenaCustodia.observaciones],
    soporteUrl = this[VistaCadenaCustodia.soporteUrl],
    hashSoporte = this[VistaCadenaCustodia.hashSoporte],
    creadoPor = this[VistaCadenaCustodia.creadoPor],
    creadoPorUsuario = this[VistaCadenaCustodia.creadoPorUsuario],
    createdAt = this[VistaCadenaCustodia.createdAt]
)