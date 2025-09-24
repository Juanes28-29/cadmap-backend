package cadmap.backend.services

import cadmap.backend.database.SesionesUsuario
import cadmap.backend.database.mappers.toSesionUsuarioDTO
import cadmap.backend.models.SesionUsuarioDTO
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq


class SesionUsuarioService {

    fun obtenerTodos(): List<SesionUsuarioDTO> = transaction {
        SesionesUsuario.selectAll().map { it.toSesionUsuarioDTO() }
    }

    fun obtenerPorId(id: UUID): SesionUsuarioDTO? = transaction {
        SesionesUsuario.select { SesionesUsuario.id eq id }
            .map { it.toSesionUsuarioDTO() }
            .singleOrNull()
    }

    fun crear(dto: SesionUsuarioDTO): Result<SesionUsuarioDTO> = runCatching {
        val id = UUID.randomUUID()
        val now = Clock.System.now()
        transaction {
            SesionesUsuario.insert {
                it[this.id] = id
                it[usuarioId] = dto.usuarioId
                it[tokenSesion] = dto.tokenSesion
                it[ipAddress] = dto.ipAddress
                it[userAgent] = dto.userAgent
                it[fechaInicio] = dto.fechaInicio ?: now
                it[fechaUltimoAcceso] = dto.fechaUltimoAcceso
                it[fechaExpiracion] = dto.fechaExpiracion
                it[activa] = dto.activa
                it[ubicacionAcceso] = dto.ubicacionAcceso
                it[dispositivoInfo] = dto.dispositivoInfo
                it[updatedAt] = now
            }
        }
        obtenerPorId(id) ?: throw Exception("No se pudo crear la sesión")
    }

    fun actualizar(id: UUID, dto: SesionUsuarioDTO): Result<Boolean> = runCatching {
        transaction {
            val updated = SesionesUsuario.update({ SesionesUsuario.id eq id }) {
                it[usuarioId] = dto.usuarioId
                it[tokenSesion] = dto.tokenSesion
                it[ipAddress] = dto.ipAddress
                it[userAgent] = dto.userAgent
                it[fechaInicio] = dto.fechaInicio
                it[fechaUltimoAcceso] = dto.fechaUltimoAcceso
                it[fechaExpiracion] = dto.fechaExpiracion
                it[activa] = dto.activa
                it[ubicacionAcceso] = dto.ubicacionAcceso
                it[dispositivoInfo] = dto.dispositivoInfo
                it[updatedAt] = Clock.System.now()
            }
            updated > 0
        }
    }

    fun eliminar(id: UUID): Result<Boolean> = runCatching {
        transaction {
            val deleted = SesionesUsuario.deleteWhere { SesionesUsuario.id eq id }
            deleted > 0
        }
    }
}
