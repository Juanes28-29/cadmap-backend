package cadmap.backend.database.mappers

import cadmap.backend.database.CausasMuerte
import cadmap.backend.models.CausaMuerteDTO
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toCausaMuerteDTO() = CausaMuerteDTO(
    id = this[CausasMuerte.id],
    codigoCie10 = this[CausasMuerte.codigoCie10],
    descripcion = this[CausasMuerte.descripcion],
    categoria = this[CausasMuerte.categoria],
    activo = this[CausasMuerte.activo]
)
