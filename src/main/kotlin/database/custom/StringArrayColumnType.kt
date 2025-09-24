package cadmap.backend.database.custom

import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.statements.api.PreparedStatementApi
import java.sql.Types

class StringArrayColumnType : ColumnType<List<String>>() {
    override fun sqlType(): String = "text[]"

    override fun valueFromDB(value: Any): List<String> = when (value) {
        is java.sql.Array -> (value.array as Array<*>).filterNotNull().map { it.toString() }
        is Array<*> -> value.filterNotNull().map { it.toString() }
        is Iterable<*> -> value.filterNotNull().map { it.toString() }
        is String -> value
            .removePrefix("{")
            .removeSuffix("}")
            .split(",")
            .filter { it.isNotBlank() }
        else -> error("Unexpected value for text[]: $value of type ${value::class}")
    }

    override fun valueToDB(value: List<String>?): Any? {
        return value?.toTypedArray()
    }

    override fun setParameter(stmt: PreparedStatementApi, index: Int, value: Any?) {
        val jdbcStmt = stmt as java.sql.PreparedStatement
        if (value == null) {
            jdbcStmt.setNull(index, Types.ARRAY)
        } else {
            val conn = jdbcStmt.connection
            val arr = conn.createArrayOf("text", (value as List<String>).toTypedArray())
            jdbcStmt.setArray(index, arr)
        }
    }
}