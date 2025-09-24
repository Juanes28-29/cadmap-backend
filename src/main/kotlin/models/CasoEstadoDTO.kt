package cadmap.backend.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import kotlinx.datetime.Instant
import java.util.UUID

@Serializable
data class CasoEstadoDTO(
    @Contextual val id: UUID? = null,
    @Contextual val casoId: UUID,
    val estadoAnterior: Int? = null,
    val estadoNuevo: Int,
    val motivo: String? = null,
    @Contextual val usuarioId: UUID,
    val fecha: Instant
)