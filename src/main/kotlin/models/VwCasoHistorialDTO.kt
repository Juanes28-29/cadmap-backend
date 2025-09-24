package cadmap.backend.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import kotlinx.datetime.Instant
import java.util.UUID

@Serializable
data class VwCasoHistorialDTO(
    @Contextual val id: UUID,
    @Contextual val casoId: UUID,
    val numeroCaso: String,
    val fecha: Instant,
    val motivo: String? = null,
    val estadoAnteriorId: Int? = null,
    val estadoAnterior: String? = null,
    val estadoNuevoId: Int? = null,
    val estadoNuevo: String? = null,
    @Contextual val usuarioId: UUID,
    val usuarioCambio: String? = null
)