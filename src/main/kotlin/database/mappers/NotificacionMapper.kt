package cadmap.backend.database.mappers

import cadmap.backend.database.Notificaciones
import cadmap.backend.models.NotificacionDTO
import org.jetbrains.exposed.sql.ResultRow
import java.util.UUID

fun ResultRow.toNotificacionDTO() = NotificacionDTO(
    id = this[Notificaciones.id],
    usuarioId = this[Notificaciones.usuarioId],
    tipo = this[Notificaciones.tipo],
    titulo = this[Notificaciones.titulo],
    mensaje = this[Notificaciones.mensaje],
    leida = this[Notificaciones.leida],
    fechaLectura = this[Notificaciones.fechaLectura],
    metadata = this[Notificaciones.metadata],
    createdAt = this[Notificaciones.createdAt]
)