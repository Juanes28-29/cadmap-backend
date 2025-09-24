package cadmap.backend.services

import cadmap.backend.database.CasoEstados
import cadmap.backend.database.mappers.toCasoEstadoDTO
import cadmap.backend.models.CasoEstadoDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class CasoEstadoService {

    fun obtenerTodos(): List<CasoEstadoDTO> = transaction {
        CasoEstados.selectAll().map { it.toCasoEstadoDTO() }
    }

    fun obtenerPorId(id: UUID): CasoEstadoDTO? = transaction {
        CasoEstados.select { CasoEstados.id eq id }
            .map { it.toCasoEstadoDTO() }
            .singleOrNull()
    }

    fun crear(dto: CasoEstadoDTO): Result<CasoEstadoDTO> = runCatching {
        val id = UUID.randomUUID()
        transaction {
            CasoEstados.insert {
                it[this.id] = id
                it[casoId] = dto.casoId
                it[estadoAnterior] = dto.estadoAnterior
                it[estadoNuevo] = dto.estadoNuevo
                it[motivo] = dto.motivo
                it[usuarioId] = dto.usuarioId
                it[fecha] = dto.fecha
            }
            obtenerPorId(id) ?: throw Exception("No se pudo crear el estado del caso")
        }
    }
}