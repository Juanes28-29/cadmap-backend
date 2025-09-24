package cadmap.backend.services

import cadmap.backend.database.Persona
import cadmap.backend.database.mappers.toPersonaDTO
import cadmap.backend.models.PersonaDTO
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class PersonaService {

    fun obtenerTodos(): List<PersonaDTO> = transaction {
        Persona.selectAll().map { it.toPersonaDTO() }
    }

    fun obtenerPorId(id: UUID): PersonaDTO? = transaction {
        Persona.select { Persona.id eq id }
            .map { it.toPersonaDTO() }
            .singleOrNull()
    }

    fun crear(dto: PersonaDTO): Result<PersonaDTO> = runCatching {
        val id = UUID.randomUUID()
        val now = Clock.System.now()
        transaction {
            Persona.insert {
                it[this.id] = id
                it[usuarioId] = dto.usuarioId
                it[tipoPersona] = dto.tipoPersona
                it[nombres] = dto.nombres
                it[apellidos] = dto.apellidos
                it[documento] = dto.documento
                it[fechaNacimiento] = dto.fechaNacimiento
                it[sexo] = dto.sexo
                it[direccion] = dto.direccion
                it[telefono] = dto.telefono
                it[email] = dto.email
                it[confidencial] = dto.confidencial
                it[createdAt] = dto.createdAt ?: now
                it[updatedAt] = dto.updatedAt ?: now
            }
        }
        obtenerPorId(id) ?: throw Exception("No se pudo crear la persona")
    }

    fun actualizar(id: UUID, dto: PersonaDTO): Result<Boolean> = runCatching {
        transaction {
            val updated = Persona.update({ Persona.id eq id }) {
                it[usuarioId] = dto.usuarioId
                it[tipoPersona] = dto.tipoPersona
                it[nombres] = dto.nombres
                it[apellidos] = dto.apellidos
                it[documento] = dto.documento
                it[fechaNacimiento] = dto.fechaNacimiento
                it[sexo] = dto.sexo
                it[direccion] = dto.direccion
                it[telefono] = dto.telefono
                it[email] = dto.email
                it[confidencial] = dto.confidencial
                it[updatedAt] = Clock.System.now()
            }
            updated > 0
        }
    }

    fun eliminar(id: UUID): Result<Boolean> = runCatching {
        transaction {
            val deleted = Persona.deleteWhere { Persona.id eq id }
            deleted > 0
        }
    }
}