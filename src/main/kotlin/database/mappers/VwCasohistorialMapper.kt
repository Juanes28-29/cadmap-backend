package cadmap.backend.database.mappers

import cadmap.backend.database.VwCasoHistorial
import cadmap.backend.models.VwCasoHistorialDTO
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toVwCasoHistorialDTO() = VwCasoHistorialDTO(
    id = this[VwCasoHistorial.id],
    casoId = this[VwCasoHistorial.casoId],
    numeroCaso = this[VwCasoHistorial.numeroCaso],
    fecha = this[VwCasoHistorial.fecha],
    motivo = this[VwCasoHistorial.motivo],
    estadoAnteriorId = this[VwCasoHistorial.estadoAnteriorId],
    estadoAnterior = this[VwCasoHistorial.estadoAnterior],
    estadoNuevoId = this[VwCasoHistorial.estadoNuevoId],
    estadoNuevo = this[VwCasoHistorial.estadoNuevo],
    usuarioId = this[VwCasoHistorial.usuarioId],
    usuarioCambio = this[VwCasoHistorial.usuarioCambio]
)