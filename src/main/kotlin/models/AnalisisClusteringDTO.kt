package cadmap.backend.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import kotlinx.datetime.Instant
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import java.util.UUID

@Serializable
data class AnalisisClusteringDTO(
    @Contextual val id: UUID? = null,
    val nombre: String,
    val descripcion: String? = null,
    val algoritmo: String,
    val parametros: JsonObject? = null,
    val fechaAnalisis: Instant? = null,
    @Contextual val analistaId: UUID,
    val areaAnalisis: String? = null,
    val periodoInicio: Instant? = null,
    val periodoFin: Instant? = null,
    val filtrosAplicados: JsonObject? = null,
    val resultadosEstadisticos: JsonObject? = null,
    val activo: Boolean,
    val createdAt: Instant? = null
)
