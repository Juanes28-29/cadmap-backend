package cadmap.backend.database.mappers

import cadmap.backend.database.PersonasCasos
import cadmap.backend.models.PersonaCasoDTO
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toPersonaCasoDTO() = PersonaCasoDTO(
    id = this[PersonasCasos.id],
    personaId = this[PersonasCasos.personaId],
    casoId = this[PersonasCasos.casoId],
    rolEnCaso = this[PersonasCasos.rolEnCaso],
    observaciones = this[PersonasCasos.observaciones],
    createdAt = this[PersonasCasos.createdAt],
    updatedAt = this[PersonasCasos.updatedAt]
)