package cadmap.backend.services

import cadmap.backend.database.PruebasLab
import cadmap.backend.database.mappers.toPruebaLabDTO
import cadmap.backend.models.PruebaLabDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq


class PruebaLabService {

    fun obtenerTodos(): List<PruebaLabDTO> = transaction {
        PruebasLab.selectAll().map { it.toPruebaLabDTO() }
    }

    fun obtenerPorId(id: UUID): PruebaLabDTO? = transaction {
        PruebasLab.select { PruebasLab.id eq id }
            .map { it.toPruebaLabDTO() }
            .singleOrNull()
    }

    fun crear(dto: PruebaLabDTO): Result<PruebaLabDTO> = runCatching {
        val id = UUID.randomUUID()
        val now = Clock.System.now()
        transaction {
            PruebasLab.insert {
                it[this.id] = id
                it[envioId] = dto.envioId
                it[evidenciaId] = dto.evidenciaId
                it[tipoPrueba] = dto.tipoPrueba
                it[metodo] = dto.metodo
                it[analista] = dto.analista
                it[fechaResultado] = dto.fechaResultado ?: now
                it[resultadoResumen] = dto.resultadoResumen
                it[archivoResultadoUrl] = dto.archivoResultadoUrl
                it[hashResultado] = dto.hashResultado
                it[estado] = dto.estado
            }
            obtenerPorId(id) ?: throw Exception("No se pudo crear la prueba de laboratorio")
        }
    }

    fun actualizar(id: UUID, dto: PruebaLabDTO): Result<Boolean> = runCatching {
        transaction {
            val updated = PruebasLab.update({ PruebasLab.id eq id }) {
                it[envioId] = dto.envioId
                it[evidenciaId] = dto.evidenciaId
                it[tipoPrueba] = dto.tipoPrueba
                it[metodo] = dto.metodo
                it[analista] = dto.analista
                it[fechaResultado] = dto.fechaResultado ?: Clock.System.now()
                it[resultadoResumen] = dto.resultadoResumen
                it[archivoResultadoUrl] = dto.archivoResultadoUrl
                it[hashResultado] = dto.hashResultado
                it[estado] = dto.estado
            }
            updated > 0
        }
    }

    fun eliminar(id: UUID): Result<Boolean> = runCatching {
        transaction {
            val deleted = PruebasLab.deleteWhere { PruebasLab.id eq id }
            deleted > 0
        }
    }
}