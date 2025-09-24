package cadmap.backend.database.mappers

import cadmap.backend.database.SesionesUsuario
import cadmap.backend.models.SesionUsuarioDTO
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toSesionUsuarioDTO() = SesionUsuarioDTO(
    id = this[SesionesUsuario.id],
    usuarioId = this[SesionesUsuario.usuarioId],
    tokenSesion = this[SesionesUsuario.tokenSesion],
    ipAddress = this[SesionesUsuario.ipAddress],
    userAgent = this[SesionesUsuario.userAgent],
    fechaInicio = this[SesionesUsuario.fechaInicio],
    fechaUltimoAcceso = this[SesionesUsuario.fechaUltimoAcceso],
    fechaExpiracion = this[SesionesUsuario.fechaExpiracion],
    activa = this[SesionesUsuario.activa],
    ubicacionAcceso = this[SesionesUsuario.ubicacionAcceso],
    dispositivoInfo = this[SesionesUsuario.dispositivoInfo],
    updatedAt = this[SesionesUsuario.updatedAt]
)
