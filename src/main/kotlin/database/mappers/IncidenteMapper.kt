package cadmap.backend.database.mappers

import cadmap.backend.database.Incidentes
import cadmap.backend.models.IncidenteDTO
import cadmap.backend.serializers.GeoJSONPoint
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toIncidenteDTO(): IncidenteDTO {
    val ubicacionRaw = this[Incidentes.ubicacion]

    val geojson = try {
        when {
            // 🧭 Caso 1: viene como GeoJSON directo (desde vista con ST_AsGeoJSON)
            ubicacionRaw.trim().startsWith("{\"type\"") -> {
                Json.decodeFromString(GeoJSONPoint.serializer(), ubicacionRaw)
            }

            // 🧩 Caso 2: viene en formato WKB hexadecimal (desde tabla base)
            ubicacionRaw.matches(Regex("^[0-9A-Fa-f]+$")) -> {
                // Si es WKB, no lo decodificamos, pero devolvemos estructura vacía válida
                GeoJSONPoint("Point", emptyList())
            }

            // 🌍 Caso 3: PostGIS devolvió texto parcial como “POINT(-75.56 6.25)”
            ubicacionRaw.startsWith("POINT(") -> {
                val coords = ubicacionRaw
                    .removePrefix("POINT(")
                    .removeSuffix(")")
                    .split(" ")
                    .mapNotNull { it.toDoubleOrNull() }

                if (coords.size == 2)
                    GeoJSONPoint("Point", coords)
                else
                    GeoJSONPoint("Point", emptyList())
            }

            // ⚙ Caso 4: valor vacío, nulo o inválido
            ubicacionRaw.isBlank() || ubicacionRaw.equals("null", true) -> {
                GeoJSONPoint("Point", emptyList())
            }

            // 🚨 Fallback (seguridad)
            else -> {
                println("⚠ Formato desconocido de ubicación: $ubicacionRaw")
                GeoJSONPoint("Point", emptyList())
            }
        }
    } catch (e: Exception) {
        println("⚠ Error al parsear GeoJSON: ${e.message}")
        GeoJSONPoint("Point", emptyList())
    }

    return IncidenteDTO(
        id = this[Incidentes.id],
        casoId = this[Incidentes.casoId],
        folioMinisterial = this[Incidentes.folioMinisterial],
        fechaHallazgo = this[Incidentes.fechaHallazgo],
        fechaLevantamiento = this[Incidentes.fechaLevantamiento],
        horaEstimadaMuerte = this[Incidentes.horaEstimadaMuerte],
        ubicacion = geojson,
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