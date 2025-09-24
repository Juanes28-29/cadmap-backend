package cadmap.backend.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import kotlinx.datetime.Instant
import java.util.UUID

@Serializable
data class UsuarioDTO(
    @Contextual val id: UUID? = null,
    val username: String,
    val email: String,
    val passwordHash: String,
    val nombre: String,
    val apellidos: String,
    val rolId: Int,
    val institucion: String,
    val cargo: String,
    val telefono: String,
    val cedulaProfesional: String? = null,
    val activo: Boolean,
    @Contextual val ultimoAcceso: Instant? = null,
    val intentosFallidos: Int,
    @Contextual val bloqueadoHasta: Instant? = null,
    @Contextual val createdAt: Instant? = null,
    @Contextual val updatedAt: Instant? = null
)
