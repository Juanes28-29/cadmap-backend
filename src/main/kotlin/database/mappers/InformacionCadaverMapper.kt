package cadmap.backend.database.mappers

import cadmap.backend.database.InformacionCadaver
import cadmap.backend.models.InformacionCadaverDTO
import org.jetbrains.exposed.sql.ResultRow
import java.util.*
import kotlinx.datetime.Instant

fun ResultRow.toInformacionCadaverDTO() = InformacionCadaverDTO(
    id = this[InformacionCadaver.id].value,
    incidenteId = this[InformacionCadaver.incidenteId],
    numeroCadaver = this[InformacionCadaver.numeroCadaver],
    sexo = this[InformacionCadaver.sexo],
    edadEstimadaMin = this[InformacionCadaver.edadEstimadaMin],
    edadEstimadaMax = this[InformacionCadaver.edadEstimadaMax],
    estaturaCm = this[InformacionCadaver.estaturaCm]?.toDouble(),
    pesoKg = this[InformacionCadaver.pesoKg]?.toDouble(),
    complexion = this[InformacionCadaver.complexion],
    colorPiel = this[InformacionCadaver.colorPiel],
    colorCabello = this[InformacionCadaver.colorCabello],
    tipoCabello = this[InformacionCadaver.tipoCabello],
    colorOjos = this[InformacionCadaver.colorOjos],
    estadoDescomposicionId = this[InformacionCadaver.estadoDescomposicionId],

    rigidezCadaverica = this[InformacionCadaver.rigidezCadaverica],
    lividecesCadavericas = this[InformacionCadaver.lividecesCadavericas],
    tatuajes = this[InformacionCadaver.tatuajes],
    cicatrices = this[InformacionCadaver.cicatrices],
    objetosPersonales = this[InformacionCadaver.objetosPersonales],

    temperaturaCorporal = this[InformacionCadaver.temperaturaCorporal]?.toDouble(),
    senasParticulares = this[InformacionCadaver.senasParticulares],
    malformaciones = this[InformacionCadaver.malformaciones],
    descripcionVestimenta = this[InformacionCadaver.descripcionVestimenta],
    posicionCadaver = this[InformacionCadaver.posicionCadaver],
    orientacionCadaver = this[InformacionCadaver.orientacionCadaver],
    descripcionPosicion = this[InformacionCadaver.descripcionPosicion],

    createdAt = this[InformacionCadaver.createdAt],
    updatedAt = this[InformacionCadaver.updatedAt],
    personaId = this[InformacionCadaver.personaId]
)