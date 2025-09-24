package cadmap.backend.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.util.*
import kotlinx.datetime.Instant
import kotlinx.serialization.json.JsonObject

@Serializable
data class InformacionCadaverDTO(
    @Contextual val id: UUID,
    @Contextual val incidenteId: UUID? = null,
    val numeroCadaver: Int? = null,
    val sexo: String? = null,
    val edadEstimadaMin: Int? = null,
    val edadEstimadaMax: Int? = null,
    val estaturaCm: Double? = null,
    val pesoKg: Double? = null,
    val complexion: String? = null,
    val colorPiel: String? = null,
    val colorCabello: String? = null,
    val tipoCabello: String? = null,
    val colorOjos: String? = null,
    val estadoDescomposicionId: Int? = null,

    // JSONB -> usar @Contextual
    @Contextual val rigidezCadaverica: JsonObject? = null,
    @Contextual val lividecesCadavericas: JsonObject? = null,
    @Contextual val tatuajes: JsonObject? = null,
    @Contextual val cicatrices: JsonObject? = null,
    @Contextual val objetosPersonales: JsonObject? = null,

    val temperaturaCorporal: Double? = null,
    val senasParticulares: String? = null,
    val malformaciones: String? = null,
    val descripcionVestimenta: String? = null,
    val posicionCadaver: String? = null,
    val orientacionCadaver: String? = null,
    val descripcionPosicion: String? = null,

    @Contextual val createdAt: Instant? = null,
    @Contextual val updatedAt: Instant? = null,
    @Contextual val personaId: UUID? = null
)