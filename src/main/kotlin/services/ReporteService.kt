package cadmap.backend.services

import cadmap.backend.database.Reportes
import cadmap.backend.database.mappers.toReporteDTO
import cadmap.backend.models.ReporteDTO
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq


class ReporteService {

    fun obtenerTodos(): List<ReporteDTO> = transaction {
        Reportes.selectAll().map { it.toReporteDTO() }
    }

    fun obtenerPorId(id: UUID): ReporteDTO? = transaction {
        Reportes.select { Reportes.id eq id }
            .map { it.toReporteDTO() }
            .singleOrNull()
    }

    fun crear(dto: ReporteDTO): Result<ReporteDTO> = runCatching {
        val id = UUID.randomUUID()
        val now = Clock.System.now()
        transaction {
            Reportes.insert {
                it[this.id] = id
                it[nombre] = dto.nombre
                it[tipoReporte] = dto.tipoReporte
                it[parametros] = dto.parametros
                it[usuarioGeneradorId] = dto.usuarioGeneradorId
                it[fechaGeneracion] = dto.fechaGeneracion ?: now
                it[archivoUrl] = dto.archivoUrl
                it[formato] = dto.formato
                it[estado] = dto.estado
                it[descargas] = dto.descargas
                it[publico] = dto.publico
                it[fechaExpiracion] = dto.fechaExpiracion
                it[updatedAt] = now
            }
        }
        obtenerPorId(id) ?: throw Exception("No se pudo crear el reporte")
    }

    fun actualizar(id: UUID, dto: ReporteDTO): Result<Boolean> = runCatching {
        transaction {
            val updated = Reportes.update({ Reportes.id eq id }) {
                it[nombre] = dto.nombre
                it[tipoReporte] = dto.tipoReporte
                it[parametros] = dto.parametros
                it[usuarioGeneradorId] = dto.usuarioGeneradorId
                it[fechaGeneracion] = dto.fechaGeneracion
                it[archivoUrl] = dto.archivoUrl
                it[formato] = dto.formato
                it[estado] = dto.estado
                it[descargas] = dto.descargas
                it[publico] = dto.publico
                it[fechaExpiracion] = dto.fechaExpiracion
                it[updatedAt] = Clock.System.now()
            }
            updated > 0
        }
    }

    fun eliminar(id: UUID): Result<Boolean> = runCatching {
        transaction {
            val deleted = Reportes.deleteWhere { Reportes.id eq id }
            deleted > 0
        }
    }
}