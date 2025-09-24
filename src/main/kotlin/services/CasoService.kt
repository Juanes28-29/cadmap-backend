package cadmap.backend.services

import cadmap.backend.database.Casos
import cadmap.backend.database.mappers.toCasoDTO
import cadmap.backend.models.CasoDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class CasoService {

    fun obtenerTodos(): List<CasoDTO> = transaction {
        Casos.selectAll().map { it.toCasoDTO() }
    }

    fun obtenerPorId(id: UUID): CasoDTO? = transaction {
        Casos
            .select { Casos.id eq id }
            .map { it.toCasoDTO() }
            .singleOrNull()
    }

    fun crear(dto: CasoDTO): Result<CasoDTO> = runCatching {
        val id = UUID.randomUUID()
        val now = Clock.System.now()
        transaction {
            Casos.insert {
                it[this.id] = id
                it[numeroCaso] = dto.numeroCaso
                it[titulo] = dto.titulo
                it[descripcionInicial] = dto.descripcionInicial
                it[tipoIncidenteId] = dto.tipoIncidenteId
                it[estado] = dto.estado
                it[prioridad] = dto.prioridad
                it[investigadorPrincipalId] = dto.investigadorPrincipalId
                it[equipoInvestigacion] = dto.equipoInvestigacion
                it[fechaApertura] = dto.fechaApertura
                it[fechaCierre] = dto.fechaCierre
                it[ubicacionGeneral] = dto.ubicacionGeneral
                it[ubicacionGeneralTxt] = dto.ubicacionGeneralTxt
                it[direccionGeneral] = dto.direccionGeneral
                it[municipio] = dto.municipio
                it[estadoProvincia] = dto.estadoProvincia
                it[pais] = dto.pais
                it[confidencial] = dto.confidencial
                it[createdAt] = now
                it[updatedAt] = now
            }
            obtenerPorId(id) ?: throw Exception("No se pudo crear el caso")
        }
    }

    fun actualizar(id: UUID, dto: CasoDTO): Result<Boolean> = runCatching {
        transaction {
            val updated = Casos.update({ Casos.id eq id }) {
                it[numeroCaso] = dto.numeroCaso
                it[titulo] = dto.titulo
                it[descripcionInicial] = dto.descripcionInicial
                it[tipoIncidenteId] = dto.tipoIncidenteId
                it[estado] = dto.estado
                it[prioridad] = dto.prioridad
                it[investigadorPrincipalId] = dto.investigadorPrincipalId
                it[equipoInvestigacion] = dto.equipoInvestigacion
                it[fechaApertura] = dto.fechaApertura
                it[fechaCierre] = dto.fechaCierre
                it[ubicacionGeneral] = dto.ubicacionGeneral
                it[ubicacionGeneralTxt] = dto.ubicacionGeneralTxt
                it[direccionGeneral] = dto.direccionGeneral
                it[municipio] = dto.municipio
                it[estadoProvincia] = dto.estadoProvincia
                it[pais] = dto.pais
                it[confidencial] = dto.confidencial
                it[updatedAt] = Clock.System.now()
            }
            updated > 0
        }
    }

    fun eliminar(id: UUID): Result<Boolean> = runCatching {
        transaction {
            val deleted = Casos.deleteWhere { Casos.id eq id }
            deleted > 0
        }
    }
}