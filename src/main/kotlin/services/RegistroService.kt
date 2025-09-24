package cadmap.backend.services

import cadmap.backend.database.Usuarios
import cadmap.backend.models.UsuarioRegisterRequest
import cadmap.backend.models.UsuarioDTO
import cadmap.backend.security.PasswordUtil
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class RegistroService {
    fun registrar(input: UsuarioRegisterRequest): Result<UsuarioDTO> = runCatching {
        val id = UUID.randomUUID()
        val now = Clock.System.now()
        val hashed = PasswordUtil.hashPassword(input.password)

        transaction {
            Usuarios.insert {
                it[Usuarios.id] = id
                it[username] = input.username
                it[email] = input.email
                it[passwordHash] = hashed
                it[nombre] = input.nombre
                it[apellidos] = input.apellidos
                it[rolId] = input.rolId
                it[institucion] = input.institucion
                it[cargo] = input.cargo
                it[telefono] = input.telefono
                it[cedulaProfesional] = input.cedulaProfesional
                it[activo] = input.activo
                it[createdAt] = now
                it[updatedAt] = now
            }
        }

        UsuarioDTO(
            id = id,
            username = input.username,
            email = input.email,
            passwordHash = hashed,
            nombre = input.nombre,
            apellidos = input.apellidos,
            rolId = input.rolId,
            institucion = input.institucion,
            cargo = input.cargo,
            telefono = input.telefono,
            cedulaProfesional = input.cedulaProfesional,
            activo = input.activo,
            createdAt = now,
            updatedAt = now,
            intentosFallidos = 0
        )
    }
}