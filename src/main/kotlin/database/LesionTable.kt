package cadmap.backend.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.UUID
import cadmap.backend.database.custom.StringArrayColumnType

object Lesion : Table("lesiones") {
    val id = uuid("id").autoGenerate()
    val cadaverId = uuid("cadaver_id")
    val tipoLesion = varchar("tipo_lesion", 50)
    val ubicacionAnatomica = varchar("ubicacion_anatomica", 100)
    val descripcionDetallada = text("descripcion_detallada")
    val dimensiones = varchar("dimensiones", 50)
    val caracteristicasEspeciales = text("caracteristicas_especiales")
    val patronLesional = text("patron_lesional")
    val vitalOPostmortem = varchar("vital_o_postmortem", 20)
    val mecanismoProduccion = text("mecanismo_produccion")
    val instrumentoProbable = text("instrumento_probable")
    val severidad = varchar("severidad", 20)
    val ordenNumeracion = integer("orden_numeracion")
    val fotografias = registerColumn<List<String>>("fotografias", StringArrayColumnType())
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")

    override val primaryKey = PrimaryKey(id)
}