package cadmap.backend.services

import cadmap.backend.database.Incidentes
import cadmap.backend.database.mappers.toIncidenteDTO
import cadmap.backend.models.IncidenteDTO
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import org.jetbrains.exposed.sql.stringLiteral
import org.jetbrains.exposed.sql.CustomFunction
import kotlinx.serialization.json.Json
import cadmap.backend.database.custom.GeometryColumnType
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import cadmap.backend.serializers.GeoJSONPoint

class IncidenteService {

    fun obtenerTodos(): List<IncidenteDTO> = transaction {
        Incidentes.selectAll().map { it.toIncidenteDTO() }
    }

    fun obtenerPorId(id: UUID): IncidenteDTO? = transaction {
        Incidentes.select { Incidentes.id eq id }
            .map { it.toIncidenteDTO() }
            .singleOrNull()
    }

    fun crear(input: IncidenteDTO): Result<IncidenteDTO> = runCatching {
        val id = UUID.randomUUID()
        val now = Clock.System.now()

        transaction {
            // Serializa el objeto GeoJSON correctamente
            val geojson = Json.encodeToString(GeoJSONPoint.serializer(), input.ubicacion)
            println("🧭 GeoJSON Kotlin serializado: $geojson")

            val literal = stringLiteral(geojson)
            println("📦 Valor literal que se envía al SQL: $literal")

            val query = "SELECT ST_GeomFromGeoJSON('$geojson');"
            println("🧪 Consulta simulada: $query")

            Incidentes.insert {
                it[Incidentes.id] = id
                it[casoId] = input.casoId
                it[folioMinisterial] = input.folioMinisterial
                it[fechaHallazgo] = input.fechaHallazgo
                it[fechaLevantamiento] = input.fechaLevantamiento
                it[horaEstimadaMuerte] = input.horaEstimadaMuerte
                it[ubicacion] = CustomFunction(
                    "ST_GeomFromGeoJSON",
                    GeometryColumnType(),
                    stringLiteral(geojson.removeSurrounding("\"")) // 🔧 evita doble escapado
                )
                it[direccionExacta] = input.direccionExacta
                it[descripcionUbicacion] = input.descripcionUbicacion
                it[accesoVehicular] = input.accesoVehicular
                it[tipoLugar] = input.tipoLugar
                it[descripcionEscena] = input.descripcionEscena
                it[condicionesClimaticas] = input.condicionesClimaticas
                it[fotografiasEscena] = input.fotografiasEscena
                it[croquisUrl] = input.croquisUrl
                it[investigadorCargoId] = input.investigadorCargoId
                it[mpCargo] = input.mpCargo
                it[peritoCargo] = input.peritoCargo
                it[createdAt] = now
                it[updatedAt] = now
            }
        }

        obtenerPorId(id) ?: error("No se pudo leer el incidente creado")
    }

    fun actualizar(id: UUID, input: IncidenteDTO): Result<Unit> = runCatching {
        val updated = transaction {
            val geojson = Json.encodeToString(GeoJSONPoint.serializer(), input.ubicacion)
            println("🧭 GeoJSON actualizado en PostGIS: $geojson") // solo para depurar

            Incidentes.update({ Incidentes.id eq id }) {
                it[casoId] = input.casoId
                it[folioMinisterial] = input.folioMinisterial
                it[fechaHallazgo] = input.fechaHallazgo
                it[fechaLevantamiento] = input.fechaLevantamiento
                it[horaEstimadaMuerte] = input.horaEstimadaMuerte
                it[ubicacion] = CustomFunction(
                    "ST_GeomFromGeoJSON",
                    GeometryColumnType(),
                    stringLiteral(geojson.removeSurrounding("\"")) // 🔧 igual que en crear()
                )
                it[direccionExacta] = input.direccionExacta
                it[descripcionUbicacion] = input.descripcionUbicacion
                it[accesoVehicular] = input.accesoVehicular
                it[tipoLugar] = input.tipoLugar
                it[descripcionEscena] = input.descripcionEscena
                it[condicionesClimaticas] = input.condicionesClimaticas
                it[fotografiasEscena] = input.fotografiasEscena
                it[croquisUrl] = input.croquisUrl
                it[investigadorCargoId] = input.investigadorCargoId
                it[mpCargo] = input.mpCargo
                it[peritoCargo] = input.peritoCargo
                it[updatedAt] = Clock.System.now()
            }
        }

        if (updated == 0) error("Incidente no encontrado")
    }

    fun eliminar(id: UUID): Result<Unit> = runCatching {
        val deleted = transaction { Incidentes.deleteWhere { Incidentes.id eq id } }
        if (deleted == 0) error("Incidente no encontrado")
    }
}