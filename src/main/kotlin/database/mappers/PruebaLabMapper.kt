package cadmap.backend.database.mappers

import cadmap.backend.database.PruebasLab
import cadmap.backend.models.PruebaLabDTO
import org.jetbrains.exposed.sql.ResultRow
import java.util.UUID

fun ResultRow.toPruebaLabDTO() = PruebaLabDTO(
    id = this[PruebasLab.id],
    envioId = this[PruebasLab.envioId],
    evidenciaId = this[PruebasLab.evidenciaId],
    tipoPrueba = this[PruebasLab.tipoPrueba],
    metodo = this[PruebasLab.metodo],
    analista = this[PruebasLab.analista],
    fechaResultado = this[PruebasLab.fechaResultado],
    resultadoResumen = this[PruebasLab.resultadoResumen],
    archivoResultadoUrl = this[PruebasLab.archivoResultadoUrl],
    hashResultado = this[PruebasLab.hashResultado],
    estado = this[PruebasLab.estado]
)