package cadmap.backend.database.mappers

import cadmap.backend.database.Evidencias
import cadmap.backend.models.EvidenciaDTO
import org.jetbrains.exposed.sql.ResultRow
import java.util.*

fun ResultRow.toEvidenciaDTO() = EvidenciaDTO(
    id = this[Evidencias.id],
    incidenteId = this[Evidencias.incidenteId],
    tipoEvidenciaId = this[Evidencias.tipoEvidenciaId],
    numeroEvidencia = this[Evidencias.numeroEvidencia],
    descripcion = this[Evidencias.descripcion],
    ubicacionHallazgo = this[Evidencias.ubicacionHallazgo],
    descripcionUbicacion = this[Evidencias.descripcionUbicacion],
    metodoRecoleccion = this[Evidencias.metodoRecoleccion],
    recipienteEmbalaje = this[Evidencias.recipienteEmbalaje],
    cadenaCustodia = this[Evidencias.cadenaCustodia],
    estadoConservacion = this[Evidencias.estadoConservacion],
    pesoGramos = this[Evidencias.pesoGramos],
    dimensiones = this[Evidencias.dimensiones],
    fotografias = this[Evidencias.fotografias],
    observaciones = this[Evidencias.observaciones],
    enviadoLaboratorio = this[Evidencias.enviadoLaboratorio],
    fechaEnvioLab = this[Evidencias.fechaEnvioLab],
    laboratorioDestino = this[Evidencias.laboratorioDestino],
    numeroLaboratorio = this[Evidencias.numeroLaboratorio],
    resultadoLaboratorio = this[Evidencias.resultadoLaboratorio],
    createdAt = this[Evidencias.createdAt],
    updatedAt = this[Evidencias.updatedAt]
)