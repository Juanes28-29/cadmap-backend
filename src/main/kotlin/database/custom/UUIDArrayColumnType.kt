package cadmap.backend.database.custom

import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.statements.api.PreparedStatementApi
import java.sql.PreparedStatement
import java.sql.Types
import java.util.*

class UUIDArrayColumnType : ColumnType<List<UUID>>() {
    override fun sqlType(): String = "UUID[]"

    override fun valueFromDB(value: Any): List<UUID> = when (value) {
        is java.sql.Array -> {
            (value.array as Array<*>)
                .filterNotNull()
                .map { UUID.fromString(it.toString()) }
        }
        is Array<*> -> value.filterNotNull().map { UUID.fromString(it.toString()) }
        is Iterable<*> -> value.filterNotNull().map { UUID.fromString(it.toString()) }
        is String -> value
            .removePrefix("{")
            .removeSuffix("}")
            .split(",")
            .filter { it.isNotBlank() }
            .map { UUID.fromString(it.trim()) }
        else -> error("Unexpected value for UUID[]: $value of type ${value::class}")
    }

    override fun valueToDB(value: List<UUID>?): Any? {
        return value?.map { it.toString() }?.toTypedArray()
    }

    override fun setParameter(stmt: PreparedStatementApi, index: Int, value: Any?) {
        val realStmt = try {
            stmt.getStatement() as PreparedStatement
        } catch (e: Exception) {
            stmt as PreparedStatement
        }

        if (value == null) {
            realStmt.setNull(index, Types.ARRAY)
        } else {
            val conn = realStmt.connection
            val arr = conn.createArrayOf(
                "UUID",
                (value as List<UUID>).map { it.toString() }.toTypedArray()
            )
            realStmt.setArray(index, arr)
        }
    }

    // 🔧 Compatibilidad: algunas versiones de Exposed no exponen getStatement()
    private fun PreparedStatementApi.getStatement(): Any? {
        return try {
            val field = this::class.java.getDeclaredField("statement")
            field.isAccessible = true
            field.get(this)
        } catch (_: Exception) {
            null
        }
    }
}