package cadmap.backend.database.mappers

import cadmap.backend.database.EnvioLab
import cadmap.backend.models.EnvioLabDTO
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toEnvioLabDTO() = EnvioLabDTO(
    id = this[EnvioLab.id],
    labId = this[EnvioLab.labId],
    remitenteId = this[EnvioLab.remitenteId],
    fechaEnvio = this[EnvioLab.fechaEnvio],
    fechaRecepcion = this[EnvioLab.fechaRecepcion],
    recibidoPor = this[EnvioLab.recibidoPor],
    numeroGuia = this[EnvioLab.numeroGuia],
    estado = this[EnvioLab.estado],
    observaciones = this[EnvioLab.observaciones]
)
