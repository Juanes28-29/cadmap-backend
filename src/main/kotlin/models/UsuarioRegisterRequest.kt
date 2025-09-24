package cadmap.backend.models

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioRegisterRequest(
    val username: String,
    val email: String,
    val password: String,       // la contraseña en texto plano, luego la hasheamos
    val nombre: String,
    val apellidos: String,
    val rolId: Int,
    val institucion: String,
    val cargo: String,
    val telefono: String,
    val cedulaProfesional: String? = null,
    val activo: Boolean = true
)