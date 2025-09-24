package cadmap.backend.services

import cadmap.backend.database.AnalisisClustering
import cadmap.backend.database.mappers.toAnalisisClusteringDTO
import cadmap.backend.models.AnalisisClusteringDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class AnalisisClusteringService {

    fun obtenerTodos(): List<AnalisisClusteringDTO> = transaction {
        AnalisisClustering.selectAll().map { it.toAnalisisClusteringDTO() }
    }

    fun obtenerPorId(id: UUID): AnalisisClusteringDTO? = transaction {
        AnalisisClustering.select { AnalisisClustering.id eq id }
            .map { it.toAnalisisClusteringDTO() }
            .singleOrNull()
    }

    fun crear(dto: AnalisisClusteringDTO): Result<AnalisisClusteringDTO> = runCatching {
        val id = UUID.randomUUID()
        val now = Clock.System.now()
        transaction {
            AnalisisClustering.insert {
                it[this.id] = id
                it[nombre] = dto.nombre
                it[descripcion] = dto.descripcion
                it[algoritmo] = dto.algoritmo
                it[parametros] = dto.parametros
                it[fechaAnalisis] = dto.fechaAnalisis
                it[analistaId] = dto.analistaId
                it[areaAnalisis] = dto.areaAnalisis
                it[periodoInicio] = dto.periodoInicio
                it[periodoFin] = dto.periodoFin
                it[filtrosAplicados] = dto.filtrosAplicados
                it[resultadosEstadisticos] = dto.resultadosEstadisticos
                it[activo] = dto.activo
                it[createdAt] = dto.createdAt ?: now
            }
        }
        obtenerPorId(id) ?: error("No se pudo crear el análisis de clustering")
    }

    fun actualizar(id: UUID, dto: AnalisisClusteringDTO): Result<Boolean> = runCatching {
        val updated = transaction {
            AnalisisClustering.update({ AnalisisClustering.id eq id }) {
                it[nombre] = dto.nombre
                it[descripcion] = dto.descripcion
                it[algoritmo] = dto.algoritmo
                it[parametros] = dto.parametros
                it[fechaAnalisis] = dto.fechaAnalisis
                it[analistaId] = dto.analistaId
                it[areaAnalisis] = dto.areaAnalisis
                it[periodoInicio] = dto.periodoInicio
                it[periodoFin] = dto.periodoFin
                it[filtrosAplicados] = dto.filtrosAplicados
                it[resultadosEstadisticos] = dto.resultadosEstadisticos
                it[activo] = dto.activo
            }
        }
        updated > 0
    }

    fun eliminar(id: UUID): Result<Boolean> = runCatching {
        val deleted = transaction {
            AnalisisClustering.deleteWhere { AnalisisClustering.id eq id }
        }
        deleted > 0
    }
}
