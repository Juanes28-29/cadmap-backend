package cadmap.backend.database

import cadmap.backend.database.custom.StringArrayColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object Incidentes : Table("incidentes") {
    val id = uuid("id").autoGenerate()
    val casoId = uuid("caso_id")
    val folioMinisterial = varchar("folio_ministerial", 255)
    val fechaHallazgo = timestamp("fecha_hallazgo")
    val fechaLevantamiento = timestamp("fecha_levantamiento")
    val horaEstimadaMuerte = timestamp("hora_estimada_muerte").nullable()
    val ubicacion = text("ubicacion")
    val direccionExacta = text("direccion_exacta")
    val descripcionUbicacion = text("descripcion_ubicacion")
    val accesoVehicular = bool("acceso_vehicular")
    val tipoLugar = varchar("tipo_lugar", 50)
    val descripcionEscena = text("descripcion_escena")
    val condicionesClimaticas = text("condiciones_climaticas")
    val fotografiasEscena = registerColumn<List<String>>("fotografias_escena", StringArrayColumnType())
    val croquisUrl = text("croquis_url").nullable()
    val investigadorCargoId = uuid("investigador_cargo_id")
    val mpCargo = varchar("mp_cargo", 100)
    val peritoCargo = varchar("perito_cargo", 100)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")

    override val primaryKey = PrimaryKey(id)
}