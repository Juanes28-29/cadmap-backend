package cadmap.backend.services

import cadmap.backend.database.InformacionCadaver
import cadmap.backend.database.mappers.toInformacionCadaverDTO
import cadmap.backend.models.InformacionCadaverDTO
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class InformacionCadaverService {

    fun obtenerTodos(): List<InformacionCadaverDTO> = transaction {
        InformacionCadaver.selectAll().map { it.toInformacionCadaverDTO() }
    }

    fun obtenerPorId(id: UUID): InformacionCadaverDTO? = transaction {
        InformacionCadaver
            .select { InformacionCadaver.id eq id }
            .map { it.toInformacionCadaverDTO() }
            .singleOrNull()
    }

    fun crear(dto: InformacionCadaverDTO): Result<InformacionCadaverDTO> = runCatching {
        val id = UUID.randomUUID()
        val now = Clock.System.now()

        transaction {
            InformacionCadaver.insert {
                it[this.id] = id
                it[incidenteId] = dto.incidenteId
                it[numeroCadaver] = dto.numeroCadaver
                it[sexo] = dto.sexo
                it[edadEstimadaMin] = dto.edadEstimadaMin
                it[edadEstimadaMax] = dto.edadEstimadaMax
                it[estaturaCm] = dto.estaturaCm?.toBigDecimal()
                it[pesoKg] = dto.pesoKg?.toBigDecimal()
                it[complexion] = dto.complexion
                it[colorPiel] = dto.colorPiel
                it[colorCabello] = dto.colorCabello
                it[tipoCabello] = dto.tipoCabello
                it[colorOjos] = dto.colorOjos
                it[estadoDescomposicionId] = dto.estadoDescomposicionId
                it[rigidezCadaverica] = dto.rigidezCadaverica
                it[lividecesCadavericas] = dto.lividecesCadavericas
                it[temperaturaCorporal] = dto.temperaturaCorporal?.toBigDecimal()
                it[senasParticulares] = dto.senasParticulares
                it[tatuajes] = dto.tatuajes
                it[cicatrices] = dto.cicatrices
                it[malformaciones] = dto.malformaciones
                it[descripcionVestimenta] = dto.descripcionVestimenta
                it[objetosPersonales] = dto.objetosPersonales
                it[posicionCadaver] = dto.posicionCadaver
                it[orientacionCadaver] = dto.orientacionCadaver
                it[descripcionPosicion] = dto.descripcionPosicion
                it[personaId] = dto.personaId
                it[createdAt] = dto.createdAt ?: now
                it[updatedAt] = dto.updatedAt ?: now
            }
            obtenerPorId(id) ?: throw Exception("No se pudo crear InformacionCadaver")
        }
    }

    fun actualizar(id: UUID, dto: InformacionCadaverDTO): Result<Boolean> = runCatching {
        val now = Clock.System.now()
        transaction {
            val updated = InformacionCadaver.update({ InformacionCadaver.id eq id }) {
                it[incidenteId] = dto.incidenteId
                it[numeroCadaver] = dto.numeroCadaver
                it[sexo] = dto.sexo
                it[edadEstimadaMin] = dto.edadEstimadaMin
                it[edadEstimadaMax] = dto.edadEstimadaMax
                it[estaturaCm] = dto.estaturaCm?.toBigDecimal()
                it[pesoKg] = dto.pesoKg?.toBigDecimal()
                it[complexion] = dto.complexion
                it[colorPiel] = dto.colorPiel
                it[colorCabello] = dto.colorCabello
                it[tipoCabello] = dto.tipoCabello
                it[colorOjos] = dto.colorOjos
                it[estadoDescomposicionId] = dto.estadoDescomposicionId
                it[rigidezCadaverica] = dto.rigidezCadaverica
                it[lividecesCadavericas] = dto.lividecesCadavericas
                it[temperaturaCorporal] = dto.temperaturaCorporal?.toBigDecimal()
                it[senasParticulares] = dto.senasParticulares
                it[tatuajes] = dto.tatuajes
                it[cicatrices] = dto.cicatrices
                it[malformaciones] = dto.malformaciones
                it[descripcionVestimenta] = dto.descripcionVestimenta
                it[objetosPersonales] = dto.objetosPersonales
                it[posicionCadaver] = dto.posicionCadaver
                it[orientacionCadaver] = dto.orientacionCadaver
                it[descripcionPosicion] = dto.descripcionPosicion
                it[personaId] = dto.personaId
                it[updatedAt] = now
            }
            updated > 0
        }
    }

    fun eliminar(id: UUID): Result<Boolean> = runCatching {
        transaction {
            val deleted = InformacionCadaver.deleteWhere { InformacionCadaver.id eq id }
            deleted > 0
        }
    }
}