package cadmap.backend.database.mappers

import cadmap.backend.database.VwCasoUltimoEstado
import cadmap.backend.models.VwCasoUltimoEstadoDTO
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toVwCasoUltimoEstadoDTO(): VwCasoUltimoEstadoDTO = VwCasoUltimoEstadoDTO(
    casoId = this[VwCasoUltimoEstado.casoId],
    numeroCaso = this[VwCasoUltimoEstado.numeroCaso],
    fechaUltimoCambio = this[VwCasoUltimoEstado.fechaUltimoCambio],
    estadoActual = this[VwCasoUltimoEstado.estadoActual],
    usuarioCambio = this[VwCasoUltimoEstado.usuarioCambio],
    motivo = this[VwCasoUltimoEstado.motivo]
)