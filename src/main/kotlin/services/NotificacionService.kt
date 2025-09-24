package cadmap.backend.services

import cadmap.backend.database.Notificaciones
import cadmap.backend.database.mappers.toNotificacionDTO
import cadmap.backend.models.NotificacionDTO
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class NotificacionService {

    fun obtenerTodos(): List<NotificacionDTO> = transaction {
        Notificaciones.selectAll().map { it.toNotificacionDTO() }
    }

    fun obtenerPorUsuario(usuarioId: UUID): List<NotificacionDTO> = transaction {
        Notificaciones
            .select { Notificaciones.usuarioId eq usuarioId }
            .map { it.toNotificacionDTO() }
    }

    fun obtenerPorId(id: UUID): NotificacionDTO? = transaction {
        Notificaciones
            .select { Notificaciones.id eq id }
            .map { it.toNotificacionDTO() }
            .singleOrNull()
    }

    fun crear(dto: NotificacionDTO): Result<NotificacionDTO> = runCatching {
        val id = UUID.randomUUID()
        val now = Clock.System.now()
        transaction {
            Notificaciones.insert {
                it[this.id] = id
                it[usuarioId] = dto.usuarioId
                it[tipo] = dto.tipo
                it[titulo] = dto.titulo
                it[mensaje] = dto.mensaje
                it[leida] = dto.leida
                it[fechaLectura] = dto.fechaLectura
                it[metadata] = dto.metadata
                it[createdAt] = dto.createdAt ?: now
            }
        }
        obtenerPorId(id) ?: throw Exception("No se pudo crear la notificación")
    }

    fun actualizar(id: UUID, dto: NotificacionDTO): Result<Boolean> = runCatching {
        transaction {
            val updated = Notificaciones.update({ Notificaciones.id eq id }) {
                it[usuarioId] = dto.usuarioId
                it[tipo] = dto.tipo
                it[titulo] = dto.titulo
                it[mensaje] = dto.mensaje
                it[leida] = dto.leida
                it[fechaLectura] = dto.fechaLectura
                it[metadata] = dto.metadata
                it[createdAt] = dto.createdAt ?: Clock.System.now()
            }
            updated > 0
        }
    }

    fun eliminar(id: UUID): Result<Boolean> = runCatching {
        transaction {
            val deleted = Notificaciones.deleteWhere { Notificaciones.id eq id }
            deleted > 0
        }
    }
}