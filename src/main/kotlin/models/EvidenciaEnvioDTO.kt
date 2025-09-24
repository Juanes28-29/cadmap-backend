package cadmap.backend.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.util.UUID

@Serializable
data class EvidenciaEnvioDTO(
    @Contextual val id: UUID? = null,
    @Contextual val envioId: UUID,
    @Contextual val evidenciaId: UUID,
    val condicionSello: String,
    val numSello: String,
    val observaciones: String? = null
)