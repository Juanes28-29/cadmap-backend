package cadmap.backend.database.mappers

import cadmap.backend.database.MecanismosMuerte
import cadmap.backend.models.MecanismoMuerteDTO
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toMecanismoMuerteDTO() = MecanismoMuerteDTO(
    id = this[MecanismosMuerte.id],
    nombre = this[MecanismosMuerte.nombre],
    descripcion = this[MecanismosMuerte.descripcion],
    categoria = this[MecanismosMuerte.categoria],
    activo = this[MecanismosMuerte.activo]
)