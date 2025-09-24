package cadmap.backend.services

import cadmap.backend.database.MecanismosMuerte
import cadmap.backend.database.mappers.toMecanismoMuerteDTO
import cadmap.backend.models.MecanismoMuerteDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class MecanismoMuerteService {

    fun obtenerTodos(): List<MecanismoMuerteDTO> = transaction {
        MecanismosMuerte.selectAll().map { it.toMecanismoMuerteDTO() }
    }

    fun obtenerPorId(id: Int): MecanismoMuerteDTO? = transaction {
        MecanismosMuerte
            .select { MecanismosMuerte.id eq id }
            .map { it.toMecanismoMuerteDTO() }
            .singleOrNull()
    }
}