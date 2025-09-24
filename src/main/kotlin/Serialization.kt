package cadmap.backend

import cadmap.backend.UUIDSerializer
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.datetime.Instant
import java.util.UUID
import kotlinx.serialization.Contextual
import kotlinx.serialization.modules.SerializersModuleBuilder

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                serializersModule = SerializersModule {
                    contextual(UUID::class, UUIDSerializer)
                    contextual(Instant::class, Instant.serializer())
                    contextual(java.math.BigDecimal::class, cadmap.backend.serializers.BigDecimalSerializer)
                }
            }
        )
    }
}