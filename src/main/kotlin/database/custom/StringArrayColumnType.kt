package cadmap.backend.database.custom

import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.statements.api.PreparedStatementApi
import java.sql.Types

class StringArrayColumnType : ColumnType<List<String>>() {
    override fun sqlType(): String = "TEXT[]"

    override fun valueFromDB(value: Any): List<String> = when (value) {
        is java.sql.Array -> (value.array as Array<*>).filterNotNull().map { it.toString() }
        is Array<*> -> value.filterNotNull().map { it.toString() }
        is Iterable<*> -> value.filterNotNull().map { it.toString() }
        is String -> value
            .removePrefix("{")
            .removeSuffix("}")
            .split(",")
            .filter { it.isNotBlank() }
            .map { it.trim() }
        else -> error("Unexpected value for TEXT[]: $value of type ${value::class}")
    }

    override fun valueToDB(value: List<String>?): Any? {
        return value?.toTypedArray()
    }

    override fun setParameter(stmt: PreparedStatementApi, index: Int, value: Any?) {
        try {
            // 🧩 Acceso seguro al PreparedStatement real
            val field = stmt.javaClass.getDeclaredField("statement")
            field.isAccessible = true
            val realStmt = field.get(stmt) as java.sql.PreparedStatement

            if (value == null) {
                realStmt.setNull(index, Types.ARRAY)
            } else {
                val conn = realStmt.connection
                val arr = conn.createArrayOf(
                    "TEXT",
                    when (value) {
                        is List<*> -> value.map { it.toString() }.toTypedArray()
                        is Array<*> -> value.map { it.toString() }.toTypedArray()
                        else -> throw IllegalArgumentException("Unsupported value type for TEXT[]: ${value::class}")
                    }
                )
                realStmt.setArray(index, arr)
            }
        } catch (e: Exception) {
            throw RuntimeException("Error setting TEXT[] parameter: ${e.message}", e)
        }
    }
}