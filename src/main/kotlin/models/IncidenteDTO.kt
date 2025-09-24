package cadmap.backend.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import kotlinx.datetime.Instant
import java.util.UUID

@Serializable
data class IncidenteDTO(
    @Contextual val id: UUID? = null,
    @Contextual val casoId: UUID,
    val folioMinisterial: String,
    @Contextual val fechaHallazgo: Instant,
    @Contextual val fechaLevantamiento: Instant,
    @Contextual val horaEstimadaMuerte: Instant? = null,
    val ubicacion: String,
    val direccionExacta: String,
    val descripcionUbicacion: String,
    val accesoVehicular: Boolean,
    val tipoLugar: String,
    val descripcionEscena: String,
    val condicionesClimaticas: String,
    val fotografiasEscena: List<String>,
    val croquisUrl: String? = null,
    @Contextual val investigadorCargoId: UUID,
    val mpCargo: String,
    val peritoCargo: String,
    @Contextual val createdAt: Instant? = null,
    @Contextual val updatedAt: Instant? = null
)