package cadmap.backend.services

import cadmap.backend.database.CadenaCustodia
import cadmap.backend.database.Usuarios
import cadmap.backend.database.mappers.toCadenaCustodiaDTO
import cadmap.backend.models.CadenaCustodiaDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import kotlinx.datetime.Clock

class CadenaCustodiaService {

    fun obtenerTodos(): List<CadenaCustodiaDTO> = transaction {
        CadenaCustodia.selectAll().map { it.toCadenaCustodiaDTO() }
    }

    fun obtenerPorId(id: UUID): CadenaCustodiaDTO? = transaction {
        CadenaCustodia
            .select { CadenaCustodia.id eq id }
            .map { it.toCadenaCustodiaDTO() }
            .singleOrNull()
    }

    fun crear(dto: CadenaCustodiaDTO): Result<CadenaCustodiaDTO> = runCatching {
        val id = UUID.randomUUID()
        val now = Clock.System.now()
        transaction {
            CadenaCustodia.insert {
                it[this.id] = id
                it[evidenciaId] = dto.evidenciaId
                it[responsableTipo] = dto.responsableTipo
                it[responsableNombre] = dto.responsableNombre
                it[fechaHora] = dto.fechaHora
                it[ubicacion] = dto.ubicacion
                it[observaciones] = dto.observaciones
                it[soporteUrl] = dto.soporteUrl
                it[hashSoporte] = dto.hashSoporte
                it[creadoPor] = dto.creadoPor
                it[createdAt] = dto.createdAt ?: now
                it[accionId] = dto.accionId
                it[ubicacionGeom] = dto.ubicacionGeom
            }
            obtenerPorId(id) ?: throw Exception("No se pudo crear la cadena de custodia")
        }
    }

    fun actualizar(id: UUID, dto: CadenaCustodiaDTO): Result<Boolean> = runCatching {
        transaction {
            val updated = CadenaCustodia.update({ CadenaCustodia.id eq id }) {
                it[evidenciaId] = dto.evidenciaId
                it[responsableTipo] = dto.responsableTipo
                it[responsableNombre] = dto.responsableNombre
                it[fechaHora] = dto.fechaHora
                it[ubicacion] = dto.ubicacion
                it[observaciones] = dto.observaciones
                it[soporteUrl] = dto.soporteUrl
                it[hashSoporte] = dto.hashSoporte
                it[creadoPor] = dto.creadoPor
                it[accionId] = dto.accionId
                it[ubicacionGeom] = dto.ubicacionGeom
            }
            updated > 0
        }
    }

    fun eliminar(id: UUID): Result<Boolean> = runCatching {
        transaction {
            val deleted = CadenaCustodia.deleteWhere { CadenaCustodia.id eq id }
            deleted > 0
        }
    }
}