package cadmap.backend.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import kotlinx.datetime.Instant

@Serializable
data class UsuarioUpdateRequest(
    val username: String,
    val email: String,
    val nombre: String,
    val apellidos: String,
    val rolId: Int,
    val institucion: String,
    val cargo: String,
    val telefono: String,
    val cedulaProfesional: String,
    val activo: Boolean,
    @Contextual val ultimoAcceso: Instant? = null,
    val intentosFallidos: Int,
    @Contextual val bloqueadoHasta: Instant? = null,
    /**
     * Si viene null -> NO cambia la contraseña.
     * Si viene con valor -> se hashea y se actualiza.
     */
    val passwordNueva: String? = null
)