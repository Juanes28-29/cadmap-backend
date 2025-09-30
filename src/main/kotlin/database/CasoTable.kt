package cadmap.backend.database

import cadmap.backend.database.custom.UUIDArrayColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.UUID

object Casos : Table("casos") {
    val id = uuid("id").autoGenerate()
    val numeroCaso = varchar("numero_caso", 50)
    val titulo = varchar("titulo", 100)
    val descripcionInicial = text("descripcion_inicial")
    val tipoIncidenteId = integer("tipo_incidente_id")
    val estado = varchar("estado", 50)
    val prioridad = varchar("prioridad", 10)
    val investigadorPrincipalId = uuid("investigador_principal_id")
    val equipoInvestigacion = registerColumn<List<UUID>>(
        "equipo_investigacion",
        UUIDArrayColumnType()
    )
    val fechaApertura = timestamp("fecha_apertura")
    val fechaCierre = timestamp("fecha_cierre").nullable()
    val ubicacionGeneral = text("ubicacion_general").nullable()
    val ubicacionGeneralTxt = text("ubicacion_general_txt").nullable()
    val direccionGeneral = text("direccion_general").nullable()
    val municipio = varchar("municipio", 100)
    val estadoProvincia = varchar("estado_provincia", 100).nullable()
    val pais = varchar("pais", 100)
    val confidencial = bool("confidencial")
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")

    override val primaryKey = PrimaryKey(id)
}