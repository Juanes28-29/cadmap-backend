package cadmap.backend.services

import cadmap.backend.database.Lesion
import cadmap.backend.database.mappers.toLesionDTO
import cadmap.backend.models.LesionDTO
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class LesionService {

    fun obtenerTodos(): List<LesionDTO> = transaction {
        Lesion.selectAll().map { it.toLesionDTO() }
    }

    fun obtenerPorId(id: UUID): LesionDTO? = transaction {
        Lesion.select { Lesion.id eq id }
            .map { it.toLesionDTO() }
            .singleOrNull()
    }

    fun crear(dto: LesionDTO): Result<LesionDTO> = runCatching {
        val id = UUID.randomUUID()
        val now = Clock.System.now()
        transaction {
            Lesion.insert {
                it[this.id] = id
                it[cadaverId] = dto.cadaverId
                it[tipoLesion] = dto.tipoLesion
                it[ubicacionAnatomica] = dto.ubicacionAnatomica
                it[descripcionDetallada] = dto.descripcionDetallada
                it[dimensiones] = dto.dimensiones
                it[caracteristicasEspeciales] = dto.caracteristicasEspeciales
                it[patronLesional] = dto.patronLesional
                it[vitalOPostmortem] = dto.vitalOPostmortem
                it[mecanismoProduccion] = dto.mecanismoProduccion
                it[instrumentoProbable] = dto.instrumentoProbable
                it[severidad] = dto.severidad
                it[ordenNumeracion] = dto.ordenNumeracion
                it[fotografias] = dto.fotografias
                it[createdAt] = dto.createdAt ?: now
                it[updatedAt] = dto.updatedAt ?: now
            }
        }
        obtenerPorId(id) ?: throw Exception("No se pudo crear la lesión")
    }

    fun actualizar(id: UUID, dto: LesionDTO): Result<Boolean> = runCatching {
        transaction {
            val updated = Lesion.update({ Lesion.id eq id }) {
                it[cadaverId] = dto.cadaverId
                it[tipoLesion] = dto.tipoLesion
                it[ubicacionAnatomica] = dto.ubicacionAnatomica
                it[descripcionDetallada] = dto.descripcionDetallada
                it[dimensiones] = dto.dimensiones
                it[caracteristicasEspeciales] = dto.caracteristicasEspeciales
                it[patronLesional] = dto.patronLesional
                it[vitalOPostmortem] = dto.vitalOPostmortem
                it[mecanismoProduccion] = dto.mecanismoProduccion
                it[instrumentoProbable] = dto.instrumentoProbable
                it[severidad] = dto.severidad
                it[ordenNumeracion] = dto.ordenNumeracion
                it[fotografias] = dto.fotografias
                it[updatedAt] = Clock.System.now()
            }
            updated > 0
        }
    }

    fun eliminar(id: UUID): Result<Boolean> = runCatching {
        transaction {
            val deleted = Lesion.deleteWhere { Lesion.id eq id }
            deleted > 0
        }
    }
}