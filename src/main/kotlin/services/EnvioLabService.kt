package cadmap.backend.services

import cadmap.backend.database.EnvioLab
import cadmap.backend.database.mappers.toEnvioLabDTO
import cadmap.backend.models.EnvioLabDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class EnvioLabService {

    fun obtenerTodos(): List<EnvioLabDTO> = transaction {
        EnvioLab.selectAll().map { it.toEnvioLabDTO() }
    }

    fun obtenerPorId(id: UUID): EnvioLabDTO? = transaction {
        EnvioLab.select { EnvioLab.id eq id }
            .map { it.toEnvioLabDTO() }
            .singleOrNull()
    }

    fun crear(dto: EnvioLabDTO): Result<EnvioLabDTO> = runCatching {
        val id = UUID.randomUUID()
        transaction {
            EnvioLab.insert {
                it[this.id] = id
                it[labId] = dto.labId
                it[remitenteId] = dto.remitenteId
                it[fechaEnvio] = dto.fechaEnvio
                it[fechaRecepcion] = dto.fechaRecepcion
                it[recibidoPor] = dto.recibidoPor
                it[numeroGuia] = dto.numeroGuia
                it[estado] = dto.estado
                it[observaciones] = dto.observaciones
            }
            obtenerPorId(id) ?: throw Exception("No se pudo crear el envío")
        }
    }

    fun actualizar(id: UUID, dto: EnvioLabDTO): Result<Boolean> = runCatching {
        transaction {
            val updated = EnvioLab.update({ EnvioLab.id eq id }) {
                it[labId] = dto.labId
                it[remitenteId] = dto.remitenteId
                it[fechaEnvio] = dto.fechaEnvio
                it[fechaRecepcion] = dto.fechaRecepcion
                it[recibidoPor] = dto.recibidoPor
                it[numeroGuia] = dto.numeroGuia
                it[estado] = dto.estado
                it[observaciones] = dto.observaciones
            }
            updated > 0
        }
    }

    fun eliminar(id: UUID): Result<Boolean> = runCatching {
        transaction {
            val deleted = EnvioLab.deleteWhere { EnvioLab.id eq id }
            deleted > 0
        }
    }
}
