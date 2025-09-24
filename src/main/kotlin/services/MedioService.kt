package cadmap.backend.services

import cadmap.backend.database.Medios
import cadmap.backend.database.mappers.toMedioDTO
import cadmap.backend.models.MedioDTO
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.UUID

class MedioService {

    fun obtenerTodos(): List<MedioDTO> = transaction {
        Medios.selectAll().map { it.toMedioDTO() }
    }

    fun obtenerPorId(id: UUID): MedioDTO? = transaction {
        Medios.select { Medios.id eq id }
            .map { it.toMedioDTO() }
            .singleOrNull()
    }

    fun crear(dto: MedioDTO): Result<MedioDTO> = runCatching {
        val id = UUID.randomUUID()
        val now = Clock.System.now()
        transaction {
            Medios.insert {
                it[this.id] = id
                it[entidad] = dto.entidad
                it[entidadId] = dto.entidadId
                it[tipo] = dto.tipo
                it[url] = dto.url
                it[mimeType] = dto.mimeType
                it[tamanoBytes] = dto.tamanoBytes
                it[hashIntegridad] = dto.hashIntegridad
                it[descripcion] = dto.descripcion
                it[creadoPor] = dto.creadoPor
                it[creadoEn] = dto.creadoEn ?: now
            }
            obtenerPorId(id) ?: throw Exception("No se pudo crear el medio")
        }
    }

    fun actualizar(id: UUID, dto: MedioDTO): Result<Boolean> = runCatching {
        transaction {
            val updated = Medios.update({ Medios.id eq id }) {
                it[entidad] = dto.entidad
                it[entidadId] = dto.entidadId
                it[tipo] = dto.tipo
                it[url] = dto.url
                it[mimeType] = dto.mimeType
                it[tamanoBytes] = dto.tamanoBytes
                it[hashIntegridad] = dto.hashIntegridad
                it[descripcion] = dto.descripcion
                it[creadoPor] = dto.creadoPor
                // no tocamos creadoEn
            }
            updated > 0
        }
    }

    fun eliminar(id: UUID): Result<Boolean> = runCatching {
        transaction {
            val deleted = Medios.deleteWhere { Medios.id eq id }
            deleted > 0
        }
    }
}