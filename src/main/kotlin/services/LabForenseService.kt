package cadmap.backend.services

import cadmap.backend.database.LabForense
import cadmap.backend.database.mappers.toLabForenseDTO
import cadmap.backend.models.LabForenseDTO
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class LabForenseService {

    fun obtenerTodos(): List<LabForenseDTO> = transaction {
        LabForense.selectAll().map { it.toLabForenseDTO() }
    }

    fun obtenerPorId(id: UUID): LabForenseDTO? = transaction {
        LabForense.select {LabForense.id eq id }
            .map { it.toLabForenseDTO() }
            .singleOrNull()
    }

    fun crear(input: LabForenseDTO): Result<LabForenseDTO> = runCatching {
        val id = UUID.randomUUID()
        val now = Clock.System.now()
        transaction {
             LabForense.insert {
                it[LabForense.id] = id
                it[nombre] = input.nombre
                it[tipo] = input.tipo
                it[institucion] = input.institucion
                it[direccion] = input.direccion
                it[ciudad] = input.ciudad
                it[telefono] = input.telefono
                it[email] = input.email
                it[creadoEn] = now
                it[creadoPor] = input.creadoPor ?: error("Debe especificar el usuario creador")
            }
        }
        obtenerPorId(id) ?: error("No se pudo leer el laboratorio creado")
    }

    fun actualizar(id: UUID, input: LabForenseDTO): Result<Unit> = runCatching {
        val updated = transaction {
            LabForense.update({LabForense.id eq id }) {
                it[nombre] = input.nombre
                it[tipo] = input.tipo
                it[institucion] = input.institucion
                it[direccion] = input.direccion
                it[ciudad] = input.ciudad
                it[telefono] = input.telefono
                it[email] = input.email
            }
        }
        if (updated == 0) error("Laboratorio no encontrado")
    }

    fun eliminar(id: UUID): Result<Unit> = runCatching {
        val deleted = transaction {LabForense.deleteWhere {LabForense.id eq id } }
        if (deleted == 0) error("Laboratorio no encontrado")
    }
}