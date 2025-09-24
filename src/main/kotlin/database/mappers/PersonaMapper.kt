package cadmap.backend.database.mappers

import cadmap.backend.database.Persona
import cadmap.backend.models.PersonaDTO
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toPersonaDTO(): PersonaDTO = PersonaDTO(
    id = this[Persona.id],
    usuarioId = this[Persona.usuarioId],
    tipoPersona = this[Persona.tipoPersona],
    nombres = this[Persona.nombres],
    apellidos = this[Persona.apellidos],
    documento = this[Persona.documento],
    fechaNacimiento = this[Persona.fechaNacimiento],
    sexo = this[Persona.sexo],
    direccion = this[Persona.direccion],
    telefono = this[Persona.telefono],
    email = this[Persona.email],
    confidencial = this[Persona.confidencial],
    createdAt = this[Persona.createdAt],
    updatedAt = this[Persona.updatedAt]
)