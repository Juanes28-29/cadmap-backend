package cadmap.backend.database.mappers

import cadmap.backend.database.Incidentes
import cadmap.backend.models.IncidenteDTO
import cadmap.backend.serializers.GeoJSONPoint
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toIncidenteDTO(): IncidenteDTO {
    val ubicacionRaw = this[Incidentes.ubicacion]

    val geojson = when {
        // ✅ Caso 1: el valor ya viene como GeoJSON
        ubicacionRaw.trim().startsWith("{") -> {
            try {
                Json.decodeFromString(GeoJSONPoint.serializer(), ubicacionRaw)
            } catch (e: Exception) {
                GeoJSONPoint("Point", emptyList())
            }
        }

        // ✅ Caso 2: viene como WKB (0101000020E61000...) -> lo dejamos vacío, pero válido
        ubicacionRaw.matches(Regex("^[0-9A-F]+$", RegexOption.IGNORE_CASE)) -> {
            GeoJSONPoint("Point", emptyList())
        }

        // ⚙ Fallback si viene nulo, vacío o con formato inesperado
        else -> GeoJSONPoint("Point", emptyList())
    }

    return IncidenteDTO(
        id = this[Incidentes.id],
        casoId = this[Incidentes.casoId],
        folioMinisterial = this[Incidentes.folioMinisterial],
        fechaHallazgo = this[Incidentes.fechaHallazgo],
        fechaLevantamiento = this[Incidentes.fechaLevantamiento],
        horaEstimadaMuerte = this[Incidentes.horaEstimadaMuerte],
        ubicacion = geojson, // 👈 aquí usamos el objeto deserializado
        direccionExacta = this[Incidentes.direccionExacta],
        descripcionUbicacion = this[Incidentes.descripcionUbicacion],
        accesoVehicular = this[Incidentes.accesoVehicular],
        tipoLugar = this[Incidentes.tipoLugar],
        descripcionEscena = this[Incidentes.descripcionEscena],
        condicionesClimaticas = this[Incidentes.condicionesClimaticas],
        fotografiasEscena = this[Incidentes.fotografiasEscena].toList(),
        croquisUrl = this[Incidentes.croquisUrl],
        investigadorCargoId = this[Incidentes.investigadorCargoId],
        mpCargo = this[Incidentes.mpCargo],
        peritoCargo = this[Incidentes.peritoCargo],
        createdAt = this[Incidentes.createdAt],
        updatedAt = this[Incidentes.updatedAt]
    )
}