package cadmap.backend.services

import cadmap.backend.database.AnalisisForenses
import cadmap.backend.database.mappers.toAnalisisForenseDTO
import cadmap.backend.models.AnalisisForenseDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class AnalisisForenseService {

    fun obtenerTodos(): List<AnalisisForenseDTO> = transaction {
        AnalisisForenses.selectAll().map { it.toAnalisisForenseDTO() }
    }

    fun obtenerPorId(id: UUID): AnalisisForenseDTO? = transaction {
        AnalisisForenses.select { AnalisisForenses.id eq id }
            .map { it.toAnalisisForenseDTO() }
            .singleOrNull()
    }

    fun crear(dto: AnalisisForenseDTO): Result<AnalisisForenseDTO> = runCatching {
        val id = UUID.randomUUID()
        val now = Clock.System.now()
        transaction {
            AnalisisForenses.insert {
                it[this.id] = id
                it[casoId] = dto.casoId
                it[tipoAnalisis] = dto.tipoAnalisis
                it[numeroDictamen] = dto.numeroDictamen
                it[fechaAnalisis] = dto.fechaAnalisis
                it[peritoResponsable] = dto.peritoResponsable
                it[laboratorio] = dto.laboratorio
                it[causaMuerteId] = dto.causaMuerteId
                it[mecanismoMuerteId] = dto.mecanismoMuerteId
                it[maneraMuerte] = dto.maneraMuerte
                it[hallazgosPrincipales] = dto.hallazgosPrincipales
                it[hallazgosMicroscopicos] = dto.hallazgosMicroscopicos
                it[resultadosToxicologicos] = dto.resultadosToxicologicos
                it[resultadosGeneticos] = dto.resultadosGeneticos
                it[conclusiones] = dto.conclusiones
                it[recomendaciones] = dto.recomendaciones
                it[archivoDictamenUrl] = dto.archivoDictamenUrl
                it[fotografiasNecropsia] = dto.fotografiasNecropsia
                it[createdAt] = dto.createdAt ?: now
                it[updatedAt] = dto.updatedAt ?: now
            }
        }
        obtenerPorId(id) ?: error("No se pudo crear el análisis forense")
    }

    fun actualizar(id: UUID, dto: AnalisisForenseDTO): Result<Boolean> = runCatching {
        val updated = transaction {
            AnalisisForenses.update({ AnalisisForenses.id eq id }) {
                it[casoId] = dto.casoId
                it[tipoAnalisis] = dto.tipoAnalisis
                it[numeroDictamen] = dto.numeroDictamen
                it[fechaAnalisis] = dto.fechaAnalisis
                it[peritoResponsable] = dto.peritoResponsable
                it[laboratorio] = dto.laboratorio
                it[causaMuerteId] = dto.causaMuerteId
                it[mecanismoMuerteId] = dto.mecanismoMuerteId
                it[maneraMuerte] = dto.maneraMuerte
                it[hallazgosPrincipales] = dto.hallazgosPrincipales
                it[hallazgosMicroscopicos] = dto.hallazgosMicroscopicos
                it[resultadosToxicologicos] = dto.resultadosToxicologicos
                it[resultadosGeneticos] = dto.resultadosGeneticos
                it[conclusiones] = dto.conclusiones
                it[recomendaciones] = dto.recomendaciones
                it[archivoDictamenUrl] = dto.archivoDictamenUrl
                it[fotografiasNecropsia] = dto.fotografiasNecropsia
                it[updatedAt] = Clock.System.now()
            }
        }
        updated > 0
    }

    fun eliminar(id: UUID): Result<Boolean> = runCatching {
        val deleted = transaction {
            AnalisisForenses.deleteWhere { AnalisisForenses.id eq id }
        }
        deleted > 0
    }
}
