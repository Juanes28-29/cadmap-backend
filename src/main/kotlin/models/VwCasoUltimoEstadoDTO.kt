package cadmap.backend.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import kotlinx.datetime.Instant
import java.util.UUID

@Serializable
data class VwCasoUltimoEstadoDTO(
    @Contextual val casoId: UUID,
    val numeroCaso: String,
    val fechaUltimoCambio: Instant,
    val estadoActual: String?,
    val usuarioCambio: String?,
    val motivo: String?
)