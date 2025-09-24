package cadmap.backend.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import kotlinx.datetime.Instant
import kotlinx.serialization.json.JsonObject
import java.util.UUID

@Serializable
data class AnalisisForenseDTO(
    @Contextual val id: UUID? = null,
    @Contextual val casoId: UUID,
    val tipoAnalisis: String,
    val numeroDictamen: String? = null,
    val fechaAnalisis: Instant? = null,
    val peritoResponsable: String? = null,
    val laboratorio: String? = null,
    val causaMuerteId: Int? = null,
    val mecanismoMuerteId: Int? = null,
    val maneraMuerte: String? = null,
    val hallazgosPrincipales: String? = null,
    val hallazgosMicroscopicos: String? = null,
    val resultadosToxicologicos: JsonObject,
    val resultadosGeneticos: JsonObject,
    val conclusiones: String? = null,
    val recomendaciones: String? = null,
    val archivoDictamenUrl: String? = null,
    val fotografiasNecropsia: List<String> = emptyList(),
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null
)
