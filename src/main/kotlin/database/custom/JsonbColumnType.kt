package cadmap.backend.database.custom

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject

class JsonbColumnType : ColumnType<JsonObject>() {

    override fun sqlType(): String = "JSONB"

    override fun valueFromDB(value: Any): JsonObject = when (value) {
        is PGobject -> {
            val raw = value.value ?: "{}"
            Json.parseToJsonElement(raw).jsonObject
        }
        is String -> Json.parseToJsonElement(value).jsonObject
        is JsonObject -> value
        else -> error("JsonbColumnType: no se puede convertir ${value::class}")
    }

    override fun notNullValueToDB(value: JsonObject): Any {
        val pg = PGobject().apply {
            type = "jsonb"
            this.value = value.toString()
        }
        return pg
    }

    override fun valueToString(value: JsonObject?): String =
        value?.toString() ?: "null"
}

/* Helper para registrar columnas JSONB */
fun Table.jsonb(name: String): Column<JsonObject> =
    registerColumn(name, JsonbColumnType())