package cadmap.backend.database.mappers

import cadmap.backend.database.Casos
import cadmap.backend.models.CasoDTO
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toCasoDTO(): CasoDTO = CasoDTO(
    id = this[Casos.id],
    numeroCaso = this[Casos.numeroCaso],
    titulo = this[Casos.titulo],
    descripcionInicial = this[Casos.descripcionInicial],
    tipoIncidenteId = this[Casos.tipoIncidenteId],
    estado = this[Casos.estado],
    prioridad = this[Casos.prioridad],
    investigadorPrincipalId = this[Casos.investigadorPrincipalId],
    equipoInvestigacion = this[Casos.equipoInvestigacion].toList(),
    fechaApertura = this[Casos.fechaApertura],
    fechaCierre = this[Casos.fechaCierre],
    ubicacionGeneral = this[Casos.ubicacionGeneral],
    ubicacionGeneralTxt = this[Casos.ubicacionGeneralTxt],
    direccionGeneral = this[Casos.direccionGeneral],
    municipio = this[Casos.municipio],
    estadoProvincia = this[Casos.estadoProvincia],
    pais = this[Casos.pais],
    confidencial = this[Casos.confidencial],
    createdAt = this[Casos.createdAt],
    updatedAt = this[Casos.updatedAt]
)