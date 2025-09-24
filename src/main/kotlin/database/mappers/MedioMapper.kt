package cadmap.backend.database.mappers

import cadmap.backend.database.Medios
import cadmap.backend.models.MedioDTO
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toMedioDTO() = MedioDTO(
    id = this[Medios.id],
    entidad = this[Medios.entidad],
    entidadId = this[Medios.entidadId],
    tipo = this[Medios.tipo],
    url = this[Medios.url],
    mimeType = this[Medios.mimeType],
    tamanoBytes = this[Medios.tamanoBytes],
    hashIntegridad = this[Medios.hashIntegridad],
    descripcion = this[Medios.descripcion],
    creadoPor = this[Medios.creadoPor],
    creadoEn = this[Medios.creadoEn]
)