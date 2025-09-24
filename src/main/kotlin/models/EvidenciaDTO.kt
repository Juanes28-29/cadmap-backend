package cadmap.backend.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.util.UUID
import java.math.BigDecimal
import kotlinx.datetime.Instant

@Serializable
data class EvidenciaDTO(
    @Contextual val id: UUID? = null,
    @Contextual val incidenteId: UUID,
    val tipoEvidenciaId: Int,
    val numeroEvidencia: String,
    val descripcion: String,
    val ubicacionHallazgo: String,
    val descripcionUbicacion: String,
    val metodoRecoleccion: String,
    val recipienteEmbalaje: String,
    val cadenaCustodia: String,
    val estadoConservacion: String,
    @Contextual val pesoGramos: BigDecimal,
    val dimensiones: String,
    val fotografias: List<String> = emptyList(),
    val observaciones: String,
    val enviadoLaboratorio: Boolean,
    val fechaEnvioLab: Instant? = null,
    val laboratorioDestino: String,
    val numeroLaboratorio: String,
    val resultadoLaboratorio: String,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null
)