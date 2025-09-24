package cadmap.backend.database.mappers

import cadmap.backend.database.LabForense
import cadmap.backend.models.LabForenseDTO
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toLabForenseDTO(): LabForenseDTO = LabForenseDTO(
    id = this[LabForense.id],
    nombre = this[LabForense.nombre],
    tipo = this[LabForense.tipo],
    institucion = this[LabForense.institucion],
    direccion = this[LabForense.direccion],
    ciudad = this[LabForense.ciudad],
    telefono = this[LabForense.telefono],
    email = this[LabForense.email],
    creadoEn = this[LabForense.creadoEn].toString(),
    creadoPor = this[LabForense.creadoPor]
)