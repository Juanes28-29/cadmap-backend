package cadmap.backend.services

import cadmap.backend.database.Usuarios
import cadmap.backend.database.mappers.toUsuarioDTO
import cadmap.backend.models.UsuarioDTO
import cadmap.backend.models.UsuarioUpdateRequest
import cadmap.backend.security.PasswordUtil
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UsuarioService {

    fun obtenerTodos(): List<UsuarioDTO> = transaction {
        Usuarios.selectAll().map { it.toUsuarioDTO() }
    }

    fun obtenerPorId(id: UUID): UsuarioDTO? = transaction {
        Usuarios.select { Usuarios.id eq id }
            .map { it.toUsuarioDTO() }
            .singleOrNull()
    }

    fun obtenerPorEmail(email: String): UsuarioDTO? = transaction {
        Usuarios.select { Usuarios.email eq email }
            .map { it.toUsuarioDTO() }
            .singleOrNull()
    }

    fun crear(input: UsuarioDTO): Result<UsuarioDTO> = runCatching {
        val id = UUID.randomUUID()
        val now = Clock.System.now()
        val hashed = PasswordUtil.hashPassword(input.passwordHash)

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
                it[ultimoAcceso] = input.ultimoAcceso
                it[intentosFallidos] = input.intentosFallidos
                it[bloqueadoHasta] = input.bloqueadoHasta
                it[createdAt] = input.createdAt ?: now
                it[updatedAt] = input.updatedAt ?: now
            }
        }

        obtenerPorId(id) ?: error("No se pudo leer el usuario creado")
    }

    /** Actualiza campos y SOLO cambia password si llega passwordNueva. */
    fun actualizar(id: UUID, input: UsuarioUpdateRequest): Result<Unit> = runCatching {
        val updated = transaction {
            Usuarios.update({ Usuarios.id eq id }) {
                it[username] = input.username
                it[email] = input.email
                it[nombre] = input.nombre
                it[apellidos] = input.apellidos
                it[rolId] = input.rolId
                it[institucion] = input.institucion
                it[cargo] = input.cargo
                it[telefono] = input.telefono
                it[cedulaProfesional] = input.cedulaProfesional
                it[activo] = input.activo
                it[ultimoAcceso] = input.ultimoAcceso
                it[intentosFallidos] = input.intentosFallidos
                it[bloqueadoHasta] = input.bloqueadoHasta
                // Solo si viene passwordNueva:
                if (!input.passwordNueva.isNullOrBlank()) {
                    it[passwordHash] = PasswordUtil.hashPassword(input.passwordNueva)
                }
                it[updatedAt] = Clock.System.now()
            }
        }
        if (updated == 0) error("Usuario no encontrado")
    }

    fun eliminar(id: UUID): Result<Unit> = runCatching {
        val deleted = transaction { Usuarios.deleteWhere { Usuarios.id eq id } }
        if (deleted == 0) error("Usuario no encontrado")
    }
}