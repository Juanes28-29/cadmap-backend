package cadmap.backend.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.UUID

object PersonasCasos : Table("personas_casos") {
    val id = uuid("id").autoGenerate()
    val personaId = uuid("persona_id")
    val casoId = uuid("caso_id")
    val rolEnCaso = varchar("rol_en_caso", 30)
    val observaciones = text("observaciones").nullable()
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at").nullable()

    override val primaryKey = PrimaryKey(id)
}