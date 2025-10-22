package cadmap.backend.database.custom

import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.statements.api.PreparedStatementApi
import java.sql.Types

class GeometryColumnType : ColumnType<String>() {

    // Tipo SQL de PostGIS
    override fun sqlType(): String = "geometry(Point, 4326)"

    // Convierte el valor devuelto por la BD (WKB -> texto)
    override fun valueFromDB(value: Any): String = value.toString()

    // ✅ Método estable sin override: maneja la serialización a DB
    fun valueToDatabase(value: Any?): Any? = value

    // Control de parámetros JDBC
    override fun setParameter(stmt: PreparedStatementApi, index: Int, value: Any?) {
        val jdbcStmt = stmt as java.sql.PreparedStatement
        if (value == null) {
            jdbcStmt.setNull(index, Types.OTHER)
        } else {
            jdbcStmt.setObject(index, value, Types.OTHER)
        }
    }
}