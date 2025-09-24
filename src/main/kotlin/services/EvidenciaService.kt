package cadmap.backend.services

import cadmap.backend.database.Evidencias
import cadmap.backend.database.mappers.toEvidenciaDTO
import cadmap.backend.models.EvidenciaDTO
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class EvidenciaService {

    fun obtenerTodos(): List<EvidenciaDTO> = transaction {
        Evidencias.selectAll().map { it.toEvidenciaDTO() }
    }

    fun obtenerPorId(id: UUID): EvidenciaDTO? = transaction {
        Evidencias.select { Evidencias.id eq id }
            .map { it.toEvidenciaDTO() }
            .singleOrNull()
    }

    fun crear(input: EvidenciaDTO): Result<EvidenciaDTO> = runCatching {
        val id = UUID.randomUUID()
        val now = Clock.System.now()
        transaction {
            Evidencias.insert {
                it[Evidencias.id] = id
                it[incidenteId] = input.incidenteId
                it[tipoEvidenciaId] = input.tipoEvidenciaId
                it[numeroEvidencia] = input.numeroEvidencia
                it[descripcion] = input.descripcion
                it[ubicacionHallazgo] = input.ubicacionHallazgo
                it[descripcionUbicacion] = input.descripcionUbicacion
                it[metodoRecoleccion] = input.metodoRecoleccion
                it[recipienteEmbalaje] = input.recipienteEmbalaje
                it[cadenaCustodia] = input.cadenaCustodia
                it[estadoConservacion] = input.estadoConservacion
                it[pesoGramos] = input.pesoGramos
                it[dimensiones] = input.dimensiones
                it[fotografias] = input.fotografias
                it[observaciones] = input.observaciones
                it[enviadoLaboratorio] = input.enviadoLaboratorio
                it[fechaEnvioLab] = input.fechaEnvioLab
                it[laboratorioDestino] = input.laboratorioDestino
                it[numeroLaboratorio] = input.numeroLaboratorio
                it[resultadoLaboratorio] = input.resultadoLaboratorio
                it[createdAt] = now
                it[updatedAt] = now
            }
        }
        obtenerPorId(id) ?: error("No se pudo leer la evidencia creada")
    }

    fun actualizar(id: UUID, input: EvidenciaDTO): Result<Unit> = runCatching {
        val updated = transaction {
            Evidencias.update({ Evidencias.id eq id }) {
                it[incidenteId] = input.incidenteId
                it[tipoEvidenciaId] = input.tipoEvidenciaId
                it[numeroEvidencia] = input.numeroEvidencia
                it[descripcion] = input.descripcion
                it[ubicacionHallazgo] = input.ubicacionHallazgo
                it[descripcionUbicacion] = input.descripcionUbicacion
                it[metodoRecoleccion] = input.metodoRecoleccion
                it[recipienteEmbalaje] = input.recipienteEmbalaje
                it[cadenaCustodia] = input.cadenaCustodia
                it[estadoConservacion] = input.estadoConservacion
                it[pesoGramos] = input.pesoGramos
                it[dimensiones] = input.dimensiones
                it[fotografias] = input.fotografias
                it[observaciones] = input.observaciones
                it[enviadoLaboratorio] = input.enviadoLaboratorio
                it[fechaEnvioLab] = input.fechaEnvioLab
                it[laboratorioDestino] = input.laboratorioDestino
                it[numeroLaboratorio] = input.numeroLaboratorio
                it[resultadoLaboratorio] = input.resultadoLaboratorio
                it[updatedAt] = Clock.System.now()
            }
        }
        if (updated == 0) error("Evidencia no encontrada")
    }

    fun eliminar(id: UUID): Result<Unit> = runCatching {
        val deleted = transaction { Evidencias.deleteWhere { Evidencias.id eq id } }
        if (deleted == 0) error("Evidencia no encontrada")
    }
}