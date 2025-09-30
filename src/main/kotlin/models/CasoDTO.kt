package cadmap.backend.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import kotlinx.datetime.Instant
import java.util.UUID

@Serializable
data class CasoDTO(
    @Contextual val id: UUID? = null,
    val numeroCaso: String,
    val titulo: String,
    val descripcionInicial: String,
    val tipoIncidenteId: Int,
    val estado: String,
    val prioridad: String,
    @Contextual val investigadorPrincipalId: UUID,
    val equipoInvestigacion: List<@Contextual UUID>,
    @Contextual val fechaApertura: Instant,
    @Contextual val fechaCierre: Instant? = null,
    val ubicacionGeneral: String? = null,
    val ubicacionGeneralTxt: String? = null,
    val direccionGeneral: String? = null,
    val municipio: String,
    val estadoProvincia: String? = null,
    val pais: String,
    val confidencial: Boolean,
    @Contextual val createdAt: Instant? = null,
    @Contextual val updatedAt: Instant? = null
)