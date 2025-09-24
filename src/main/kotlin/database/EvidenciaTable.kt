package cadmap.backend.database

import cadmap.backend.database.custom.StringArrayColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.math.BigDecimal
import java.util.UUID

object Evidencias : Table("evidencias") {
    val id = uuid("id").autoGenerate()
    val incidenteId = uuid("incidente_id").references(Incidentes.id)
    val tipoEvidenciaId = integer("tipo_evidencia_id")
    val numeroEvidencia = varchar("numero_evidencia", 20)
    val descripcion = text("descripcion")
    val ubicacionHallazgo = text("ubicacion_hallazgo") // 👈 usarías geometry con PostGIS, aquí simplificado
    val descripcionUbicacion = text("descripcion_ubicacion")
    val metodoRecoleccion = text("metodo_recoleccion")
    val recipienteEmbalaje = varchar("recipiente_embalaje", 100)
    val cadenaCustodia = text("cadena_custodia") // 👈 jsonb → manejar como String o JsonElement
    val estadoConservacion = varchar("estado_conservacion", 50)
    val pesoGramos = decimal("peso_gramos", 8, 3)
    val dimensiones = varchar("dimensiones", 100)
    val fotografias = registerColumn<List<String>>("fotografias", StringArrayColumnType())
    val observaciones = text("observaciones")
    val enviadoLaboratorio = bool("enviado_laboratorio")
    val fechaEnvioLab = timestamp("fecha_envio_lab").nullable()
    val laboratorioDestino = varchar("laboratorio_destino", 100)
    val numeroLaboratorio = varchar("numero_laboratorio", 50)
    val resultadoLaboratorio = text("resultado_laboratorio")

    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")

    override val primaryKey = PrimaryKey(id)
}