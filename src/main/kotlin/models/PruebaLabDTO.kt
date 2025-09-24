package cadmap.backend.models

import io.ktor.http.ContentType
import io.ktor.websocket.Frame
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import kotlinx.datetime.Instant
import org.w3c.dom.Text
import java.util.UUID

@Serializable
data class PruebaLabDTO(
    @Contextual val id: UUID,
    @Contextual val envioId: UUID,
    @Contextual val evidenciaId: UUID,
    val tipoPrueba: String,
    val metodo: String,
    val analista: String,
    val fechaResultado: Instant? = null,
    val resultadoResumen: String?,
    val archivoResultadoUrl: String?,
    val hashResultado: String?,
    val estado: String
)