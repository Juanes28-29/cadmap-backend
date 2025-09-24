package cadmap.backend.services

import cadmap.backend.database.IncidenteCluster
import cadmap.backend.database.mappers.toIncidenteClusterDTO
import cadmap.backend.models.IncidenteClusterDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class IncidenteClusterService {

    fun obtenerTodos(): List<IncidenteClusterDTO> = transaction {
        IncidenteCluster.selectAll().map { it.toIncidenteClusterDTO() }
    }

    fun obtenerPorId(id: UUID): IncidenteClusterDTO? = transaction {
        IncidenteCluster.select { IncidenteCluster.id eq id }
            .map { it.toIncidenteClusterDTO() }
            .singleOrNull()
    }

    fun crear(dto: IncidenteClusterDTO): Result<IncidenteClusterDTO> = runCatching {
        val id = UUID.randomUUID()
        val now = Clock.System.now()
        transaction {
            IncidenteCluster.insert {
                it[this.id] = id
                it[incidenteId] = dto.incidenteId
                it[clusterId] = dto.clusterId
                it[distanciaCentroide] = dto.distanciaCentroide
                it[probabilidadPertenencia] = dto.probabilidadPertenencia
                it[createdAt] = dto.createdAt ?: now
            }
            obtenerPorId(id) ?: throw Exception("No se pudo crear incidente_cluster")
        }
    }

    fun actualizar(id: UUID, dto: IncidenteClusterDTO): Result<Boolean> = runCatching {
        transaction {
            val updated = IncidenteCluster.update({ IncidenteCluster.id eq id }) {
                it[incidenteId] = dto.incidenteId
                it[clusterId] = dto.clusterId
                it[distanciaCentroide] = dto.distanciaCentroide
                it[probabilidadPertenencia] = dto.probabilidadPertenencia
            }
            updated > 0
        }
    }

    fun eliminar(id: UUID): Result<Boolean> = runCatching {
        transaction {
            val deleted = IncidenteCluster.deleteWhere { IncidenteCluster.id eq id }
            deleted > 0
        }
    }
}