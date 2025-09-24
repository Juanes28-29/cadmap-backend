package cadmap.backend.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import kotlinx.datetime.Instant
import java.util.UUID

@Serializable
data class VistaCadenaCustodiaDTO(
    @Contextual val id: UUID,
    @Contextual val evidenciaId: UUID? = null,
    val numeroEvidencia: String? = null,
    @Contextual val incidenteId: UUID? = null,
    val accionId: Int? = null,
    val accionCodigo: String? = null,
    val accionDescripcion: String? = null,
    val responsableTipo: String? = null,
    @Contextual val responsableId: UUID? = null,
    val responsableUsuario: String? = null,
    val responsableNombre: String? = null,
    val fechaHora: Instant? = null,
    val ubicacion: String? = null,
    val ubicacionWkt: String? = null,
    val observaciones: String? = null,
    val soporteUrl: String? = null,
    val hashSoporte: String? = null,
    @Contextual val creadoPor: UUID? = null,
    val creadoPorUsuario: String? = null,
    val createdAt: Instant? = null
)