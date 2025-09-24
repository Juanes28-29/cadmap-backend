package cadmap.backend.services

import cadmap.backend.database.EvidenciasEnvios
import cadmap.backend.database.mappers.toEvidenciaEnvioDTO
import cadmap.backend.models.EvidenciaEnvioDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class EvidenciaEnvioService {

    fun obtenerTodos(): List<EvidenciaEnvioDTO> = transaction {
        EvidenciasEnvios.selectAll().map { it.toEvidenciaEnvioDTO() }
    }

    fun obtenerPorId(id: UUID): EvidenciaEnvioDTO? = transaction {
        EvidenciasEnvios
            .select { EvidenciasEnvios.id eq id }
            .map { it.toEvidenciaEnvioDTO() }
            .singleOrNull()
    }

    fun crear(dto: EvidenciaEnvioDTO): Result<EvidenciaEnvioDTO> = runCatching {
        val id = UUID.randomUUID()
        transaction {
            EvidenciasEnvios.insert {
                it[this.id] = id
                it[envioId] = dto.envioId
                it[evidenciaId] = dto.evidenciaId
                it[condicionSello] = dto.condicionSello
                it[numSello] = dto.numSello
                it[observaciones] = dto.observaciones
            }
            obtenerPorId(id) ?: throw Exception("No se pudo crear el registro de evidencia_envio")
        }
    }

    fun actualizar(id: UUID, dto: EvidenciaEnvioDTO): Result<Boolean> = runCatching {
        transaction {
            val updated = EvidenciasEnvios.update({ EvidenciasEnvios.id eq id }) {
                it[envioId] = dto.envioId
                it[evidenciaId] = dto.evidenciaId
                it[condicionSello] = dto.condicionSello
                it[numSello] = dto.numSello
                it[observaciones] = dto.observaciones
            }
            updated > 0
        }
    }

    fun eliminar(id: UUID): Result<Boolean> = runCatching {
        transaction {
            val deleted = EvidenciasEnvios.deleteWhere { EvidenciasEnvios.id eq id }
            deleted > 0
        }
    }
}