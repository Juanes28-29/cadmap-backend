package cadmap.backend.database.custom

import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.statements.api.PreparedStatementApi
import java.sql.Types

class GeometryColumnType : ColumnType<String>() {

    override fun sqlType(): String = "geometry(Point, 4326)"

    override fun valueFromDB(value: Any): String = value.toString()
    fun valueToDB(value: Any?): Any? = value

    override fun setParameter(stmt: PreparedStatementApi, index: Int, value: Any?) {
        val jdbcStmt = stmt as java.sql.PreparedStatement
        if (value == null) {
            jdbcStmt.setNull(index, Types.OTHER)
        } else {
            jdbcStmt.setObject(index, value, Types.OTHER)
        }
    }
}