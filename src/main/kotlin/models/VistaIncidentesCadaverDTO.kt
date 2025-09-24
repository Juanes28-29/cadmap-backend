package cadmap.backend.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import kotlinx.datetime.Instant
import java.util.UUID

@Serializable
data class VistaIncidentesCadaverDTO(
    @Contextual val incidenteId: UUID,
    val folioMinisterial: String? = null,
    val fechaHallazgo: Instant? = null,
    val ubicacion: String? = null,
    val direccionExacta: String? = null,
    val sexo: String? = null,
    val edadEstimadaMin: Int? = null,
    val edadEstimadaMax: Int? = null,
    val estadoDescomposicion: String? = null,
    val causaMuerteId: Int? = null,
    val maneraMuerte: String? = null,
    val numeroCaso: String? = null
)