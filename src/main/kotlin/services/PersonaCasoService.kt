package cadmap.backend.services

import cadmap.backend.database.PersonasCasos
import cadmap.backend.database.mappers.toPersonaCasoDTO
import cadmap.backend.models.PersonaCasoDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import kotlinx.datetime.Clock
import java.util.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class PersonaCasoService {

    fun obtenerTodos(): List<PersonaCasoDTO> = transaction {
        PersonasCasos.selectAll().map { it.toPersonaCasoDTO() }
    }

    fun obtenerPorId(id: UUID): PersonaCasoDTO? = transaction {
        PersonasCasos.select { PersonasCasos.id eq id }
            .map { it.toPersonaCasoDTO() }
            .singleOrNull()
    }

    fun crear(dto: PersonaCasoDTO): Result<PersonaCasoDTO> = runCatching {
        val id = UUID.randomUUID()
        val now = Clock.System.now()
        transaction {
            PersonasCasos.insert {
                it[this.id] = id
                it[personaId] = dto.personaId
                it[casoId] = dto.casoId
                it[rolEnCaso] = dto.rolEnCaso
                it[observaciones] = dto.observaciones
                it[createdAt] = now
                it[updatedAt] = now
            }
            obtenerPorId(id) ?: throw Exception("No se pudo crear la relación persona-caso")
        }
    }

    fun actualizar(id: UUID, dto: PersonaCasoDTO): Result<Boolean> = runCatching {
        val now = Clock.System.now()
        transaction {
            val updated = PersonasCasos.update({ PersonasCasos.id eq id }) {
                it[personaId] = dto.personaId
                it[casoId] = dto.casoId
                it[rolEnCaso] = dto.rolEnCaso
                it[observaciones] = dto.observaciones
                it[updatedAt] = now
            }
            updated > 0
        }
    }

    fun eliminar(id: UUID): Result<Boolean> = runCatching {
        transaction {
            val deleted = PersonasCasos.deleteWhere { PersonasCasos.id eq id }
            deleted > 0
        }
    }
}