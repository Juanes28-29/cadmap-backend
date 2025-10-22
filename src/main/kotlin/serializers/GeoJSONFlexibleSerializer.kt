package cadmap.backend.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

@Serializable
data class GeoJSONPoint(
    val type: String = "Point",
    val coordinates: List<Double> = emptyList()
)

/**
 * Serializer que acepta tanto:
 *  - Un objeto JSON ({"type":"Point", "coordinates":[-75,6]})
 *  - Como una cadena JSON escapada ("{\"type\":\"Point\",\"coordinates\":[-75,6]}")
 */
object GeoJSONFlexibleSerializer : KSerializer<GeoJSONPoint> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("GeoJSONFlexible", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): GeoJSONPoint {
        val jsonDecoder = decoder as? JsonDecoder
            ?: throw SerializationException("Solo puede usarse con JSON")

        val element = jsonDecoder.decodeJsonElement()
        return when (element) {
            is JsonObject -> {
                // Asegura que siempre tenga "type": "Point"
                val map = element.toMutableMap()
                val type = (map["type"] as? JsonPrimitive)?.content ?: "Point"
                val coords = map["coordinates"] ?: JsonArray(emptyList())
                GeoJSONPoint(type, Json.decodeFromJsonElement(coords))
            }
            is JsonPrimitive -> {
                val content = element.content
                try {
                    val parsed = Json.decodeFromString(GeoJSONPoint.serializer(), content)
                    parsed.copy(type = parsed.type.ifBlank { "Point" })
                } catch (e: Exception) {
                    throw SerializationException("GeoJSON inválido: $content")
                }
            }
            else -> GeoJSONPoint("Point", emptyList())
        }
    }

    override fun serialize(encoder: Encoder, value: GeoJSONPoint) {
        val jsonEncoder = encoder as? JsonEncoder
            ?: throw SerializationException("Solo puede usarse con JSON")
        val obj = Json.encodeToJsonElement(GeoJSONPoint.serializer(), value)
        jsonEncoder.encodeJsonElement(obj)
    }
}