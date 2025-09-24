package cadmap.backend.database.custom

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.util.UUID

// Extensión para que Exposed entienda uuid[]
fun Table.uuidArray(name: String): Column<List<UUID>> =
    registerColumn(name, UUIDArrayColumnType())