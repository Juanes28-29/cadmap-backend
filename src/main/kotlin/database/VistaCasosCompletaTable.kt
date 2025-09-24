package cadmap.backend.database

import cadmap.backend.database.custom.UUIDArrayColumnType
import cadmap.backend.database.custom.StringArrayColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.UUID

object VistaCasosCompleta : Table("vista_casos_completa") {
    val id = uuid("id")
    val numeroCaso = varchar("numero_caso", 50)
    val titulo = varchar("titulo", 100)
    val descripcionInicial = text("descripcion_inicial")
    val tipoIncidenteId = integer("tipo_incidente_id").nullable()
    val tipoIncidenteCodigo = varchar("tipo_incidente_codigo", 50).nullable()
    val tipoIncidenteNombre = varchar("tipo_incidente_nombre", 100).nullable()
    val estadoActualTexto = varchar("estado_actual_texto", 100).nullable()
    val estadoUltimoCatalogo = varchar("estado_ultimo_catalogo", 100).nullable()
    val estadoUltimoUsuario = varchar("estado_ultimo_usuario", 100).nullable()
    val fechaCambioEstado = timestamp("fecha_cambio_estado").nullable()
    val investigadorPrincipalId = uuid("investigador_principal_id").nullable()
    val investigadorUsername = varchar("investigador_username", 100).nullable()
    val investigadorNombreCompleto = varchar("investigador_nombre_completo", 200).nullable()

    // UUID[]
    val equipoInvestigacion = registerColumn<List<UUID>>(
        "equipo_investigacion", UUIDArrayColumnType()
    ).nullable()

    // TEXT[]
    val equipoNombres = registerColumn<List<String>>(
        "equipo_nombres", StringArrayColumnType()
    ).nullable()

    val ubicacionGeneral = text("ubicacion_general")
    val municipio = varchar("municipio", 100)
    val pais = varchar("pais", 100)
    val confidencial = bool("confidencial")
    val incidentesCount = long("incidentes_count")
    val fechaApertura = timestamp("fecha_apertura")
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
}