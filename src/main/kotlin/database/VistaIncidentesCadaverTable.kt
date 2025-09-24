package cadmap.backend.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.UUID

object VistaIncidentesCadaver : Table("vista_incidentes_cadaver") {
    val incidenteId = uuid("incidente_id")
    val folioMinisterial = varchar("folio_ministerial", 255).nullable()
    val fechaHallazgo = timestamp("fecha_hallazgo").nullable()
    val ubicacion = text("ubicacion").nullable()
    val direccionExacta = text("direccion_exacta").nullable()
    val sexo = varchar("sexo", 50).nullable()
    val edadEstimadaMin = integer("edad_estimada_min").nullable()
    val edadEstimadaMax = integer("edad_estimada_max").nullable()
    val estadoDescomposicion = varchar("estado_descomposicion", 255).nullable()
    val causaMuerteId = integer("causa_muerte_id").nullable()
    val maneraMuerte = varchar("manera_muerte", 255).nullable()
    val numeroCaso = varchar("numero_caso", 100).nullable()
}