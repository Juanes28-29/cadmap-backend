package cadmap.backend.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import kotlinx.datetime.Instant
import java.util.UUID

@Serializable
data class CadenaCustodiaDTO(
    @Contextual val id: UUID? = null,
    @Contextual val evidenciaId: UUID,
    val responsableTipo: String,
    val responsableNombre: String,
    val fechaHora: Instant,
    val ubicacion: String,
    val observaciones: String,
    val soporteUrl: String,
    val hashSoporte: String,
    @Contextual val creadoPor: UUID? = null,
    val createdAt: Instant? = null,
    val accionId: Int,
    val ubicacionGeom: String // 👈 en PostGIS geometry lo manejamos como WKT (Well-Known Text) en el DTO
)