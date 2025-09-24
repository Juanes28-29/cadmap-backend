package cadmap.backend.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import kotlinx.datetime.Instant
import java.util.UUID

@Serializable
data class VistaCasosCompletaDTO(
    @Contextual val id: UUID,
    val numeroCaso: String,
    val titulo: String,
    val descripcionInicial: String,
    val tipoIncidenteId: Int? = null,
    val tipoIncidenteCodigo: String? = null,
    val tipoIncidenteNombre: String? = null,
    val estadoActualTexto: String? = null,
    val estadoUltimoCatalogo: String? = null,
    val estadoUltimoUsuario: String? = null,
    val fechaCambioEstado: Instant? = null,
    @Contextual val investigadorPrincipalId: UUID? = null,
    val investigadorUsername: String? = null,
    val investigadorNombreCompleto: String? = null,
    val equipoInvestigacion: List<@Contextual UUID>? = emptyList(),
    val equipoNombres: List<String>? = emptyList(),
    val ubicacionGeneral: String,
    val municipio: String,
    val pais: String,
    val confidencial: Boolean,
    val incidentesCount: Long,
    val fechaApertura: Instant,
    val createdAt: Instant,
    val updatedAt: Instant
)