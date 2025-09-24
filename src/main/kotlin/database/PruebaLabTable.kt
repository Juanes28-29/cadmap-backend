package cadmap.backend.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.UUID

object PruebasLab : Table("pruebas_lab") {
    val id = uuid("id").autoGenerate()
    val envioId = uuid("envio_id")
    val evidenciaId = uuid("evidencia_id")
    val tipoPrueba = varchar("tipo_prueba", 80)
    val metodo = text("metodo")
    val analista = text("analista")
    val fechaResultado = timestamp("fecha_resultado").nullable()
    val resultadoResumen = text("resultado_resumen").nullable()
    val archivoResultadoUrl = text("archivo_resultado_url").nullable()
    val hashResultado = text("hash_resultado").nullable()
    val estado = varchar("estado", 20)

    override val primaryKey = PrimaryKey(id)
}