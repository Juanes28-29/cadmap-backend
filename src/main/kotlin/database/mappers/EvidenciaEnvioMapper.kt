package cadmap.backend.database.mappers

import cadmap.backend.database.EvidenciasEnvios
import cadmap.backend.models.EvidenciaEnvioDTO
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toEvidenciaEnvioDTO() = EvidenciaEnvioDTO(
    id = this[EvidenciasEnvios.id].value,
    envioId = this[EvidenciasEnvios.envioId],
    evidenciaId = this[EvidenciasEnvios.evidenciaId],
    condicionSello = this[EvidenciasEnvios.condicionSello],
    numSello = this[EvidenciasEnvios.numSello],
    observaciones = this[EvidenciasEnvios.observaciones]
)