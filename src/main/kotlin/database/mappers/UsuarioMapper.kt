package cadmap.backend.database.mappers
import cadmap.backend.database.Usuarios
import cadmap.backend.models.UsuarioDTO
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toUsuarioDTO(): UsuarioDTO = UsuarioDTO(
    id = this[Usuarios.id],
    username = this[Usuarios.username],
    email = this[Usuarios.email],
    passwordHash = this[Usuarios.passwordHash],
    nombre = this[Usuarios.nombre],
    apellidos = this[Usuarios.apellidos],
    rolId = this[Usuarios.rolId],
    institucion = this[Usuarios.institucion],
    cargo = this[Usuarios.cargo],
    telefono = this[Usuarios.telefono],
    cedulaProfesional = this[Usuarios.cedulaProfesional],
    activo = this[Usuarios.activo],
    ultimoAcceso = this[Usuarios.ultimoAcceso],
    intentosFallidos = this[Usuarios.intentosFallidos],
    bloqueadoHasta = this[Usuarios.bloqueadoHasta],
    createdAt = this[Usuarios.createdAt],
    updatedAt = this[Usuarios.updatedAt]
)
