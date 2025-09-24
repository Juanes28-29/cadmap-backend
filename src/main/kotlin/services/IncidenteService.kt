package cadmap.backend.services

import cadmap.backend.database.Incidentes
import cadmap.backend.database.mappers.toIncidenteDTO
import cadmap.backend.models.IncidenteDTO
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class IncidenteService {

    fun obtenerTodos(): List<IncidenteDTO> = transaction {
        Incidentes.selectAll().map { it.toIncidenteDTO() }
    }

    fun obtenerPorId(id: UUID): IncidenteDTO? = transaction {
        Incidentes.select { Incidentes.id eq id }
            .map { it.toIncidenteDTO() }
            .singleOrNull()
    }

    fun crear(input: IncidenteDTO): Result<IncidenteDTO> = runCatching {
        val id = UUID.randomUUID()
        val now = Clock.System.now()
        transaction {
            Incidentes.insert {
                it[Incidentes.id] = id
                it[casoId] = input.casoId
                it[folioMinisterial] = input.folioMinisterial
                it[fechaHallazgo] = input.fechaHallazgo
                it[fechaLevantamiento] = input.fechaLevantamiento
                it[horaEstimadaMuerte] = input.horaEstimadaMuerte
                it[ubicacion] = input.ubicacion
                it[direccionExacta] = input.direccionExacta
                it[descripcionUbicacion] = input.descripcionUbicacion
                it[accesoVehicular] = input.accesoVehicular
                it[tipoLugar] = input.tipoLugar
                it[descripcionEscena] = input.descripcionEscena
                it[condicionesClimaticas] = input.condicionesClimaticas
                it[fotografiasEscena] = input.fotografiasEscena
                it[croquisUrl] = input.croquisUrl
                it[investigadorCargoId] = input.investigadorCargoId
                it[mpCargo] = input.mpCargo
                it[peritoCargo] = input.peritoCargo
                it[createdAt] = now
                it[updatedAt] = now
            }
        }
        obtenerPorId(id) ?: error("No se pudo leer el incidente creado")
    }

    fun actualizar(id: UUID, input: IncidenteDTO): Result<Unit> = runCatching {
        val updated = transaction {
            Incidentes.update({ Incidentes.id eq id }) {
                it[casoId] = input.casoId
                it[folioMinisterial] = input.folioMinisterial
                it[fechaHallazgo] = input.fechaHallazgo
                it[fechaLevantamiento] = input.fechaLevantamiento
                it[horaEstimadaMuerte] = input.horaEstimadaMuerte
                it[ubicacion] = input.ubicacion
                it[direccionExacta] = input.direccionExacta
                it[descripcionUbicacion] = input.descripcionUbicacion
                it[accesoVehicular] = input.accesoVehicular
                it[tipoLugar] = input.tipoLugar
                it[descripcionEscena] = input.descripcionEscena
                it[condicionesClimaticas] = input.condicionesClimaticas
                it[fotografiasEscena] = input.fotografiasEscena
                it[croquisUrl] = input.croquisUrl
                it[investigadorCargoId] = input.investigadorCargoId
                it[mpCargo] = input.mpCargo
                it[peritoCargo] = input.peritoCargo
                it[updatedAt] = Clock.System.now()
            }
        }
        if (updated == 0) error("Incidente no encontrado")
    }

    fun eliminar(id: UUID): Result<Unit> = runCatching {
        val deleted = transaction { Incidentes.deleteWhere { Incidentes.id eq id } }
        if (deleted == 0) error("Incidente no encontrado")
    }
}