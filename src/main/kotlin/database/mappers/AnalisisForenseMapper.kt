package cadmap.backend.database.mappers

import cadmap.backend.database.AnalisisForenses
import cadmap.backend.models.AnalisisForenseDTO
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toAnalisisForenseDTO() = AnalisisForenseDTO(
    id = this[AnalisisForenses.id],
    casoId = this[AnalisisForenses.casoId],
    tipoAnalisis = this[AnalisisForenses.tipoAnalisis],
    numeroDictamen = this[AnalisisForenses.numeroDictamen],
    fechaAnalisis = this[AnalisisForenses.fechaAnalisis],
    peritoResponsable = this[AnalisisForenses.peritoResponsable],
    laboratorio = this[AnalisisForenses.laboratorio],
    causaMuerteId = this[AnalisisForenses.causaMuerteId],
    mecanismoMuerteId = this[AnalisisForenses.mecanismoMuerteId],
    maneraMuerte = this[AnalisisForenses.maneraMuerte],
    hallazgosPrincipales = this[AnalisisForenses.hallazgosPrincipales],
    hallazgosMicroscopicos = this[AnalisisForenses.hallazgosMicroscopicos],
    resultadosToxicologicos = this[AnalisisForenses.resultadosToxicologicos],
    resultadosGeneticos = this[AnalisisForenses.resultadosGeneticos],
    conclusiones = this[AnalisisForenses.conclusiones],
    recomendaciones = this[AnalisisForenses.recomendaciones],
    archivoDictamenUrl = this[AnalisisForenses.archivoDictamenUrl],
    fotografiasNecropsia = this[AnalisisForenses.fotografiasNecropsia],
    createdAt = this[AnalisisForenses.createdAt],
    updatedAt = this[AnalisisForenses.updatedAt]
)
