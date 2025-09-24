package cadmap.backend.database.mappers

import cadmap.backend.database.AnalisisClustering
import cadmap.backend.models.AnalisisClusteringDTO
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toAnalisisClusteringDTO() = AnalisisClusteringDTO(
    id = this[AnalisisClustering.id],
    nombre = this[AnalisisClustering.nombre],
    descripcion = this[AnalisisClustering.descripcion],
    algoritmo = this[AnalisisClustering.algoritmo],
    parametros = this[AnalisisClustering.parametros],
    fechaAnalisis = this[AnalisisClustering.fechaAnalisis],
    analistaId = this[AnalisisClustering.analistaId],
    areaAnalisis = this[AnalisisClustering.areaAnalisis],
    periodoInicio = this[AnalisisClustering.periodoInicio],
    periodoFin = this[AnalisisClustering.periodoFin],
    filtrosAplicados = this[AnalisisClustering.filtrosAplicados],
    resultadosEstadisticos = this[AnalisisClustering.resultadosEstadisticos],
    activo = this[AnalisisClustering.activo],
    createdAt = this[AnalisisClustering.createdAt]
)
