package cadmap.backend.database.mappers

import cadmap.backend.database.Reportes
import cadmap.backend.models.ReporteDTO
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toReporteDTO() = ReporteDTO(
    id = this[Reportes.id],
    nombre = this[Reportes.nombre],
    tipoReporte = this[Reportes.tipoReporte],
    parametros = this[Reportes.parametros],
    usuarioGeneradorId = this[Reportes.usuarioGeneradorId],
    fechaGeneracion = this[Reportes.fechaGeneracion],
    archivoUrl = this[Reportes.archivoUrl],
    formato = this[Reportes.formato],
    estado = this[Reportes.estado],
    descargas = this[Reportes.descargas],
    publico = this[Reportes.publico],
    fechaExpiracion = this[Reportes.fechaExpiracion],
    updatedAt = this[Reportes.updatedAt]
)