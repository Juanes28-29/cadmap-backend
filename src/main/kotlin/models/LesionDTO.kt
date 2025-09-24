package cadmap.backend.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.util.UUID
import kotlinx.datetime.Instant

@Serializable
data class LesionDTO(
    @Contextual val id: UUID? = null,
    @Contextual val cadaverId: UUID,
    val tipoLesion: String,
    val ubicacionAnatomica: String,
    val descripcionDetallada: String,
    val dimensiones: String,
    val caracteristicasEspeciales: String,
    val patronLesional: String,
    val vitalOPostmortem: String,
    val mecanismoProduccion: String,
    val instrumentoProbable: String,
    val severidad: String,
    val ordenNumeracion: Int,
    val fotografias: List<String> = emptyList(),
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null
)