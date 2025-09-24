package cadmap.backend.database.mappers

import cadmap.backend.database.VistaCasosCompleta
import cadmap.backend.models.VistaCasosCompletaDTO
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toVistaCasosCompletaDTO() = VistaCasosCompletaDTO(
    id = this[VistaCasosCompleta.id],
    numeroCaso = this[VistaCasosCompleta.numeroCaso],
    titulo = this[VistaCasosCompleta.titulo],
    descripcionInicial = this[VistaCasosCompleta.descripcionInicial],
    tipoIncidenteId = this[VistaCasosCompleta.tipoIncidenteId],
    tipoIncidenteCodigo = this[VistaCasosCompleta.tipoIncidenteCodigo],
    tipoIncidenteNombre = this[VistaCasosCompleta.tipoIncidenteNombre],
    estadoActualTexto = this[VistaCasosCompleta.estadoActualTexto],
    estadoUltimoCatalogo = this[VistaCasosCompleta.estadoUltimoCatalogo],
    estadoUltimoUsuario = this[VistaCasosCompleta.estadoUltimoUsuario],
    fechaCambioEstado = this[VistaCasosCompleta.fechaCambioEstado],
    investigadorPrincipalId = this[VistaCasosCompleta.investigadorPrincipalId],
    investigadorUsername = this[VistaCasosCompleta.investigadorUsername],
    investigadorNombreCompleto = this[VistaCasosCompleta.investigadorNombreCompleto],
    equipoInvestigacion = this[VistaCasosCompleta.equipoInvestigacion],
    equipoNombres = this[VistaCasosCompleta.equipoNombres],
    ubicacionGeneral = this[VistaCasosCompleta.ubicacionGeneral],
    municipio = this[VistaCasosCompleta.municipio],
    pais = this[VistaCasosCompleta.pais],
    confidencial = this[VistaCasosCompleta.confidencial],
    incidentesCount = this[VistaCasosCompleta.incidentesCount],
    fechaApertura = this[VistaCasosCompleta.fechaApertura],
    createdAt = this[VistaCasosCompleta.createdAt],
    updatedAt = this[VistaCasosCompleta.updatedAt]
)