package cadmap.backend.database.mappers

import cadmap.backend.database.CasoEstados
import cadmap.backend.models.CasoEstadoDTO
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toCasoEstadoDTO() = CasoEstadoDTO(
    id = this[CasoEstados.id],
    casoId = this[CasoEstados.casoId],
    estadoAnterior = this[CasoEstados.estadoAnterior],
    estadoNuevo = this[CasoEstados.estadoNuevo],
    motivo = this[CasoEstados.motivo],
    usuarioId = this[CasoEstados.usuarioId],
    fecha = this[CasoEstados.fecha]
)