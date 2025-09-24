package cadmap.backend.services

import cadmap.backend.database.Clusters
import cadmap.backend.database.mappers.toClusterDTO
import cadmap.backend.models.ClusterDTO
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.UUID

class ClusterService {

    fun obtenerTodos(): List<ClusterDTO> = transaction {
        Clusters.selectAll().map { it.toClusterDTO() }
    }

    fun obtenerPorId(id: UUID): ClusterDTO? = transaction {
        Clusters
            .select { Clusters.id eq id }
            .map { it.toClusterDTO() }
            .singleOrNull()
    }

    fun crear(dto: ClusterDTO): Result<ClusterDTO> = runCatching {
        val id = UUID.randomUUID()
        val now = Clock.System.now()
        transaction {
            Clusters.insert {
                it[this.id] = id
                it[analisisId] = dto.analisisId
                it[numeroCluster] = dto.numeroCluster
                it[centroide] = dto.centroide
                it[areaInfluencia] = dto.areaInfluencia
                it[numeroIncidentes] = dto.numeroIncidentes
                it[densidad] = dto.densidad
                it[radioMetros] = dto.radioMetros
                it[nivelRiesgo] = dto.nivelRiesgo
                it[descripcion] = dto.descripcion
                it[recomendaciones] = dto.recomendaciones
                it[colorVisualizacion] = dto.colorVisualizacion
                it[activo] = dto.activo
                it[createdAt] = dto.createdAt ?: now
            }
            obtenerPorId(id) ?: throw Exception("No se pudo crear el cluster")
        }
    }

    fun actualizar(id: UUID, dto: ClusterDTO): Result<Boolean> = runCatching {
        transaction {
            val updated = Clusters.update({ Clusters.id eq id }) {
                it[analisisId] = dto.analisisId
                it[numeroCluster] = dto.numeroCluster
                it[centroide] = dto.centroide
                it[areaInfluencia] = dto.areaInfluencia
                it[numeroIncidentes] = dto.numeroIncidentes
                it[densidad] = dto.densidad
                it[radioMetros] = dto.radioMetros
                it[nivelRiesgo] = dto.nivelRiesgo
                it[descripcion] = dto.descripcion
                it[recomendaciones] = dto.recomendaciones
                it[colorVisualizacion] = dto.colorVisualizacion
                it[activo] = dto.activo
            }
            updated > 0
        }
    }

    fun eliminar(id: UUID): Result<Boolean> = runCatching {
        transaction {
            val deleted = Clusters.deleteWhere { Clusters.id eq id }
            deleted > 0
        }
    }
}