package cadmap.backend.database

import cadmap.backend.database.custom.jsonb
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.*

object InformacionCadaver : UUIDTable("informacion_cadaver") {
    val incidenteId = uuid("incidente_id").nullable()
    val numeroCadaver = integer("numero_cadaver").nullable()
    val sexo = varchar("sexo", 20).nullable()
    val edadEstimadaMin = integer("edad_estimada_min").nullable()
    val edadEstimadaMax = integer("edad_estimada_max").nullable()
    val estaturaCm = decimal("estatura_cm", 5, 2).nullable()
    val pesoKg = decimal("peso_kg", 5, 2).nullable()
    val complexion = varchar("complexion", 20).nullable()
    val colorPiel = varchar("color_piel", 30).nullable()
    val colorCabello = varchar("color_cabello", 30).nullable()
    val tipoCabello = varchar("tipo_cabello", 30).nullable()
    val colorOjos = varchar("color_ojos", 20).nullable()
    val estadoDescomposicionId = integer("estado_descomposicion_id").nullable()

    // Campos JSONB → ahora funcionan como Map<String, Any>
    val rigidezCadaverica = jsonb("rigidez_cadaverica").nullable()
    val lividecesCadavericas = jsonb("livideces_cadavericas").nullable()
    val tatuajes = jsonb("tatuajes").nullable()
    val cicatrices = jsonb("cicatrices").nullable()
    val objetosPersonales = jsonb("objetos_personales").nullable()

    val temperaturaCorporal = decimal("temperatura_corporal", 4, 2).nullable()
    val senasParticulares = text("señas_particulares").nullable()
    val malformaciones = text("malformaciones").nullable()
    val descripcionVestimenta = text("descripcion_vestimenta").nullable()
    val posicionCadaver = varchar("posicion_cadaver", 50).nullable()
    val orientacionCadaver = varchar("orientacion_cadaver", 50).nullable()
    val descripcionPosicion = text("descripcion_posicion").nullable()
    val createdAt = timestamp("created_at").defaultExpression(org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestamp)
    val updatedAt = timestamp("updated_at").nullable()
    val personaId = uuid("persona_id").nullable()
}