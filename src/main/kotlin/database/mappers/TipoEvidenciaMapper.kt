package cadmap.backend.database.mappers

import cadmap.backend.database.TiposEvidencia
import cadmap.backend.models.TipoEvidenciaDTO
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toTipoEvidenciaDTO() = TipoEvidenciaDTO(
    id = this[TiposEvidencia.id],
    nombre = this[TiposEvidencia.nombre],
    categoria = this[TiposEvidencia.categoria],
    descripcion = this[TiposEvidencia.descripcion],
    requiereCadenaCustodia = this[TiposEvidencia.requiereCadenaCustodia],
    activo = this[TiposEvidencia.activo]
)
