package cadmap.backend

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.http.*
import java.util.*
import cadmap.backend.security.JwtConfig
import cadmap.backend.security.PasswordUtil
import cadmap.backend.security.authorize
import cadmap.backend.security.Roles
import cadmap.backend.models.LoginRequest
import cadmap.backend.models.UsuarioDTO
import cadmap.backend.models.UsuarioUpdateRequest
import cadmap.backend.services.UsuarioService
import cadmap.backend.models.CasoDTO
import cadmap.backend.services.CasoService
import cadmap.backend.models.IncidenteDTO
import cadmap.backend.services.IncidenteService
import cadmap.backend.services.LabForenseService
import cadmap.backend.models.LabForenseDTO
import cadmap.backend.models.EvidenciaDTO
import cadmap.backend.services.EvidenciaService
import cadmap.backend.services.CadenaCustodiaService
import cadmap.backend.models.CadenaCustodiaDTO
import cadmap.backend.services.LesionService
import cadmap.backend.models.LesionDTO
import cadmap.backend.models.PersonaDTO
import cadmap.backend.services.PersonaService
import cadmap.backend.models.InformacionCadaverDTO
import cadmap.backend.services.InformacionCadaverService
import cadmap.backend.models.IncidenteClusterDTO
import cadmap.backend.services.IncidenteClusterService
import cadmap.backend.models.EnvioLabDTO
import cadmap.backend.services.EnvioLabService
import cadmap.backend.services.EvidenciaEnvioService
import cadmap.backend.models.EvidenciaEnvioDTO
import cadmap.backend.models.ClusterDTO
import cadmap.backend.services.ClusterService
import cadmap.backend.models.MedioDTO
import cadmap.backend.services.MedioService
import cadmap.backend.services.NotificacionService
import cadmap.backend.models.NotificacionDTO
import cadmap.backend.models.ReporteDTO
import cadmap.backend.services.ReporteService
import cadmap.backend.models.SesionUsuarioDTO
import cadmap.backend.services.SesionUsuarioService
import cadmap.backend.services.TipoEvidenciaService
import cadmap.backend.services.TipoIncidenteService
import cadmap.backend.services.CatAccionCustodiaService
import cadmap.backend.services.CatEstadoCasoService
import cadmap.backend.services.AnalisisClusteringService
import cadmap.backend.models.AnalisisClusteringDTO
import cadmap.backend.models.AnalisisForenseDTO
import cadmap.backend.services.AnalisisForenseService
import cadmap.backend.services.CausaMuerteService
import cadmap.backend.services.CasoEstadoService
import cadmap.backend.models.CasoEstadoDTO
import cadmap.backend.services.EstadoDescomposicionService
import cadmap.backend.services.LogAuditoriaService
import cadmap.backend.services.MecanismoMuerteService
import cadmap.backend.services.PruebaLabService
import cadmap.backend.models.PruebaLabDTO
import cadmap.backend.services.PersonaCasoService
import cadmap.backend.models.PersonaCasoDTO
import cadmap.backend.services.VistaCadenaCustodiaService
import cadmap.backend.services.VistaCasosCompletaService
import cadmap.backend.services.VistaIncidentesCadaverService
import cadmap.backend.services.VwCasoHistorialService
import cadmap.backend.services.VwCasoUltimoEstadoService
import cadmap.backend.models.UsuarioRegisterRequest
import cadmap.backend.services.RegistroService

fun Application.configureRouting() {
    routing {
        val usuarioService = UsuarioService()
        get("/estado") {
            call.respondText("CadMap backend activo")
        }

        post("/register") {
            val request = call.receive<UsuarioRegisterRequest>()
            val registroService = RegistroService()

            val resultado = registroService.registrar(request)
            resultado.fold(
                onSuccess = { call.respond(HttpStatusCode.Created, it) },
                onFailure = { call.respond(HttpStatusCode.InternalServerError, "Error al registrar: ${it.message}") }
            )
        }

        post("/login") {
            val request = call.receive<LoginRequest>()
            val usuario = usuarioService.obtenerPorEmail(request.email)

            if (usuario == null) {
                call.respond(HttpStatusCode.Unauthorized, "Usuario no encontrado")
                return@post
            }

            try {
                println("DEBUG email: ${request.email}")
                println("DEBUG plain password: ${request.password}")
                println("DEBUG hash in DB: ${usuario.passwordHash}")

                val ok = PasswordUtil.verifyPassword(request.password, usuario.passwordHash)
                if (!ok) {
                    call.respond(HttpStatusCode.Unauthorized, "Contraseña incorrecta")
                    return@post
                }
            } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, "Error en login: ${e.message}")
            }

            val token = JwtConfig.generateToken(
                email = usuario.email,
                username = usuario.username,
                rol = usuario.rolId.toString()
            )

            call.respond(HttpStatusCode.OK, mapOf("token" to token))
        }
        post("/debug-login") {
            val log = call.application.environment.log
            try {
                val req = call.receive<LoginRequest>()
                val usuario = usuarioService.obtenerPorEmail(req.email)

                if (usuario == null) {
                    call.respond(HttpStatusCode.NotFound, mapOf(
                        "error" to "Usuario no encontrado",
                        "email" to req.email
                    ))
                    return@post
                }

                val ok = PasswordUtil.verifyPassword(req.password, usuario.passwordHash)

                call.respond(HttpStatusCode.OK, mapOf(
                    "email" to usuario.email,
                    "hash" to usuario.passwordHash,
                    "passwordCorrecta" to ok,
                    "nota" to "Si passwordCorrecta=false, el hash en DB no corresponde a la contraseña enviada."
                ))
            } catch (e: Exception) {
                log.error("Error en /debug-login", e)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to (e.message ?: "unknown"), "stacktrace" to e.stackTraceToString())
                )
            }
        }

        authenticate("auth-jwt") {

            // Usuario autenticado
            get("/me") {
                val principal = call.principal<JWTPrincipal>()
                val email = principal?.getClaim("email", String::class)
                call.respondText("Hola, tu correo es $email")
            }

            // ====== USUARIOS ======

            // Listar (ADMIN o ANALISTA)
            get("/usuarios") {
                call.authorize(Roles.ADMIN, Roles.ANALISTA) {
                    try {
                        val usuarios = usuarioService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, usuarios)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        call.respond(
                            HttpStatusCode.InternalServerError,
                            "Error al obtener usuarios: ${e.message}"
                        )
                    }
                }
            }

            // Obtener por ID (ADMIN o ANALISTA)
            get("/usuarios/{id}") {
                call.authorize(Roles.ADMIN, Roles.ANALISTA) {
                    val idParam = call.parameters["id"]
                    val id = try {
                        UUID.fromString(idParam)
                    } catch (_: Exception) {
                        call.respond(HttpStatusCode.BadRequest, "ID inválido")
                        return@authorize
                    }

                    val usuario = usuarioService.obtenerPorId(id)
                    if (usuario != null) {
                        call.respond(HttpStatusCode.OK, usuario)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Usuario no encontrado")
                    }
                }
            }

            // Crear (solo ADMIN)
            post("/usuarios") {
                call.authorize(Roles.ADMIN) {
                    val dto = call.receive<UsuarioDTO>()
                    val created = usuarioService.crear(dto)
                    created.onSuccess { nuevo ->
                        call.respond(HttpStatusCode.Created, nuevo)
                    }.onFailure { e ->
                        call.respond(HttpStatusCode.BadRequest, "No se pudo crear: ${e.message}")
                    }
                }
            }

            // Actualizar (solo ADMIN)
            put("/usuarios/{id}") {
                call.authorize(Roles.ADMIN) {
                    val idParam = call.parameters["id"]
                    val id = try {
                        UUID.fromString(idParam)
                    } catch (_: Exception) {
                        call.respond(HttpStatusCode.BadRequest, "ID inválido")
                        return@authorize
                    }

                    val body = call.receive<UsuarioUpdateRequest>()
                    val result = usuarioService.actualizar(id, body)
                    result.onSuccess {
                        call.respond(HttpStatusCode.OK, "Usuario actualizado")
                    }.onFailure { e ->
                        call.respond(
                            if (e.message?.contains("no encontrado", ignoreCase = true) == true)
                                HttpStatusCode.NotFound else HttpStatusCode.BadRequest,
                            e.message ?: "No se pudo actualizar"
                        )
                    }
                }
            }

            // Eliminar (solo ADMIN)
            delete("/usuarios/{id}") {
                call.authorize(Roles.ADMIN) {
                    val idParam = call.parameters["id"]
                    val id = try {
                        UUID.fromString(idParam)
                    } catch (_: Exception) {
                        call.respond(HttpStatusCode.BadRequest, "ID inválido")
                        return@authorize
                    }

                    val result = usuarioService.eliminar(id)
                    result.onSuccess {
                        call.respond(HttpStatusCode.OK, "Usuario eliminado")
                    }.onFailure { e ->
                        call.respond(
                            if (e.message?.contains("no encontrado", ignoreCase = true) == true)
                                HttpStatusCode.NotFound else HttpStatusCode.BadRequest,
                            e.message ?: "Usuario no encontrado"
                        )
                    }
                }
            }
            route("/casos") {
                val casoService = CasoService()

                // Listar todos (ADMIN, ANALISTA)
                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA) {
                        val casos = casoService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, casos)
                    }
                }

                // Obtener por ID (ADMIN, ANALISTA, FORENSE)
                get("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val caso = casoService.obtenerPorId(id)
                        if (caso != null) {
                            call.respond(HttpStatusCode.OK, caso)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Caso no encontrado")
                        }
                    }
                }

                // Crear nuevo caso (FORENSE, ADMIN)
                post {
                    call.authorize(Roles.FORENSE, Roles.ADMIN) {
                        val dto = call.receive<CasoDTO>()
                        val created = casoService.crear(dto)
                        created.onSuccess { nuevo ->
                            call.respond(HttpStatusCode.Created, nuevo)
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, "No se pudo crear: ${e.message}")
                        }
                    }
                }

                // Actualizar caso (FORENSE, ADMIN)
                put("{id}") {
                    call.authorize(Roles.FORENSE, Roles.ADMIN) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val dto = call.receive<CasoDTO>()
                        val result = casoService.actualizar(id, dto)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Caso actualizado")
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, e.message ?: "No se pudo actualizar")
                        }
                    }
                }

                // Eliminar caso (solo ADMIN)
                delete("{id}") {
                    call.authorize(Roles.ADMIN) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val result = casoService.eliminar(id)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Caso eliminado")
                        }.onFailure { e ->
                            call.respond(
                                if (e.message?.contains("no encontrado", ignoreCase = true) == true)
                                    HttpStatusCode.NotFound else HttpStatusCode.BadRequest,
                                e.message ?: "Caso no encontrado"
                            )
                        }
                    }
                }
            }
            route("/incidentes") {
                val incidenteService = IncidenteService()

                // Listar todos (ADMIN, ANALISTA)
                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA) {
                        val incidentes = incidenteService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, incidentes)
                    }
                }

                // Obtener por ID (ADMIN, ANALISTA, FORENSE)
                get("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }
                        val incidente = incidenteService.obtenerPorId(id)
                        if (incidente != null) {
                            call.respond(HttpStatusCode.OK, incidente)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Incidente no encontrado")
                        }
                    }
                }
                // Crear nuevo incidente (FORENSE, ADMIN)
                post {
                    call.authorize(Roles.FORENSE, Roles.ADMIN) {
                        val dto = call.receive<IncidenteDTO>()
                        val created = incidenteService.crear(dto)
                        created.onSuccess { nuevo ->
                            call.respond(HttpStatusCode.Created, nuevo)
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, "No se pudo crear: ${e.message}")
                        }
                    }
                }
                // Actualizar incidente (FORENSE, ADMIN)
                put("{id}") {
                    call.authorize(Roles.FORENSE, Roles.ADMIN) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val dto = call.receive<IncidenteDTO>()
                        val result = incidenteService.actualizar(id, dto)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Incidente actualizado")
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, e.message ?: "No se pudo actualizar")
                        }
                    }
                }
                // Eliminar incidente (solo ADMIN)
                delete("{id}") {
                    call.authorize(Roles.ADMIN) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val result = incidenteService.eliminar(id)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Incidente eliminado")
                        }.onFailure { e ->
                            call.respond(
                                if (e.message?.contains("no encontrado", ignoreCase = true) == true)
                                    HttpStatusCode.NotFound else HttpStatusCode.BadRequest,
                                e.message ?: "Incidente no encontrado"
                            )
                        }
                    }
                }
            }
            route("/labs_forenses") {
                val labService = LabForenseService()

                // Listar todos (ADMIN, ANALISTA)
                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA) {
                        val labs = labService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, labs)
                    }
                }

                // Obtener por ID (ADMIN, ANALISTA, FORENSE)
                get("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val lab = labService.obtenerPorId(id)
                        if (lab != null) {
                            call.respond(HttpStatusCode.OK, lab)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Laboratorio no encontrado")
                        }
                    }
                }

                // Crear nuevo laboratorio (solo ADMIN)
                post {
                    call.authorize(Roles.ADMIN) {
                        val dto = call.receive<LabForenseDTO>()
                        val created = labService.crear(dto)
                        created.onSuccess { nuevo ->
                            call.respond(HttpStatusCode.Created, nuevo)
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, "No se pudo crear: ${e.message}")
                        }
                    }
                }

                // Actualizar laboratorio (solo ADMIN)
                put("{id}") {
                    call.authorize(Roles.ADMIN) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val dto = call.receive<LabForenseDTO>()
                        val result = labService.actualizar(id, dto)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Laboratorio actualizado")
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, e.message ?: "No se pudo actualizar")
                        }
                    }
                }

                // Eliminar laboratorio (solo ADMIN)
                delete("{id}") {
                    call.authorize(Roles.ADMIN) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val result = labService.eliminar(id)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Laboratorio eliminado")
                        }.onFailure { e ->
                            call.respond(
                                if (e.message?.contains("no encontrado", ignoreCase = true) == true)
                                    HttpStatusCode.NotFound else HttpStatusCode.BadRequest,
                                e.message ?: "Laboratorio no encontrado"
                            )
                        }
                    }
                }
            }
            route("/evidencias") {
                val evidenciaService = EvidenciaService()

                // Listar todos (ADMIN, ANALISTA)
                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA) {
                        val evidencias = evidenciaService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, evidencias)
                    }
                }

                // Obtener por ID (ADMIN, ANALISTA, FORENSE)
                get("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val evidencia = evidenciaService.obtenerPorId(id)
                        if (evidencia != null) {
                            call.respond(HttpStatusCode.OK, evidencia)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Evidencia no encontrada")
                        }
                    }
                }

                // Crear nueva evidencia (FORENSE, ADMIN)
                post {
                    call.authorize(Roles.FORENSE, Roles.ADMIN) {
                        val dto = call.receive<EvidenciaDTO>()
                        val created = evidenciaService.crear(dto)
                        created.onSuccess { nueva ->
                            call.respond(HttpStatusCode.Created, nueva)
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, "No se pudo crear: ${e.message}")
                        }
                    }
                }

                // Actualizar evidencia (FORENSE, ADMIN)
                put("{id}") {
                    call.authorize(Roles.FORENSE, Roles.ADMIN) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val dto = call.receive<EvidenciaDTO>()
                        val result = evidenciaService.actualizar(id, dto)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Evidencia actualizada")
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, e.message ?: "No se pudo actualizar")
                        }
                    }
                }

                // Eliminar evidencia (solo ADMIN)
                delete("{id}") {
                    call.authorize(Roles.ADMIN) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val result = evidenciaService.eliminar(id)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Evidencia eliminada")
                        }.onFailure { e ->
                            call.respond(
                                if (e.message?.contains("no encontrada", ignoreCase = true) == true)
                                    HttpStatusCode.NotFound else HttpStatusCode.BadRequest,
                                e.message ?: "Evidencia no encontrada"
                            )
                        }
                    }
                }
            }
            route("/cadenas_custodia") {
                val cadenaCustodiaService = CadenaCustodiaService()

                // Listar todos (ADMIN, ANALISTA)
                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA) {
                        val cadenas = cadenaCustodiaService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, cadenas)
                    }
                }

                // Obtener por ID (ADMIN, ANALISTA, FORENSE)
                get("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val cadena = cadenaCustodiaService.obtenerPorId(id)
                        if (cadena != null) {
                            call.respond(HttpStatusCode.OK, cadena)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Cadena de custodia no encontrada")
                        }
                    }
                }

                // Crear (FORENSE, ADMIN)
                post {
                    call.authorize(Roles.FORENSE, Roles.ADMIN) {
                        val dto = call.receive<CadenaCustodiaDTO>()
                        val created = cadenaCustodiaService.crear(dto)
                        created.onSuccess { nueva ->
                            call.respond(HttpStatusCode.Created, nueva)
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, "No se pudo crear: ${e.message}")
                        }
                    }
                }

                // Actualizar (FORENSE, ADMIN)
                put("{id}") {
                    call.authorize(Roles.FORENSE, Roles.ADMIN) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val dto = call.receive<CadenaCustodiaDTO>()
                        val result = cadenaCustodiaService.actualizar(id, dto)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Cadena de custodia actualizada")
                        }.onFailure { e ->
                            call.respond(
                                if (e.message?.contains("no encontrado", ignoreCase = true) == true)
                                    HttpStatusCode.NotFound else HttpStatusCode.BadRequest,
                                e.message ?: "No se pudo actualizar"
                            )
                        }
                    }
                }

                // Eliminar (solo ADMIN)
                delete("{id}") {
                    call.authorize(Roles.ADMIN) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val result = cadenaCustodiaService.eliminar(id)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Cadena de custodia eliminada")
                        }.onFailure { e ->
                            call.respond(
                                if (e.message?.contains("no encontrado", ignoreCase = true) == true)
                                    HttpStatusCode.NotFound else HttpStatusCode.BadRequest,
                                e.message ?: "Cadena de custodia no encontrada"
                            )
                        }
                    }
                }
            }
            route("/lesiones") {
                val lesionService = LesionService()

                // Listar todos (ADMIN, ANALISTA)
                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA) {
                        val lesiones = lesionService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, lesiones)
                    }
                }

                // Obtener por ID (ADMIN, ANALISTA, FORENSE)
                get("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val lesion = lesionService.obtenerPorId(id)
                        if (lesion != null) {
                            call.respond(HttpStatusCode.OK, lesion)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Lesión no encontrada")
                        }
                    }
                }

                // Crear (FORENSE, ADMIN)
                post {
                    call.authorize(Roles.FORENSE, Roles.ADMIN) {
                        val dto = call.receive<LesionDTO>()
                        val created = lesionService.crear(dto)
                        created.onSuccess { nueva ->
                            call.respond(HttpStatusCode.Created, nueva)
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, "No se pudo crear: ${e.message}")
                        }
                    }
                }

                // Actualizar (FORENSE, ADMIN)
                put("{id}") {
                    call.authorize(Roles.FORENSE, Roles.ADMIN) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val dto = call.receive<LesionDTO>()
                        val result = lesionService.actualizar(id, dto)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Lesión actualizada")
                        }.onFailure { e ->
                            call.respond(
                                if (e.message?.contains("no encontrado", ignoreCase = true) == true)
                                    HttpStatusCode.NotFound else HttpStatusCode.BadRequest,
                                e.message ?: "No se pudo actualizar"
                            )
                        }
                    }
                }

                // Eliminar (solo ADMIN)
                delete("{id}") {
                    call.authorize(Roles.ADMIN) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val result = lesionService.eliminar(id)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Lesión eliminada")
                        }.onFailure { e ->
                            call.respond(
                                if (e.message?.contains("no encontrado", ignoreCase = true) == true)
                                    HttpStatusCode.NotFound else HttpStatusCode.BadRequest,
                                e.message ?: "Lesión no encontrada"
                            )
                        }
                    }
                }
            }
            route("/personas") {
                val personaService = PersonaService()

                // Listar todos (ADMIN, ANALISTA)
                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA) {
                        val personas = personaService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, personas)
                    }
                }

                // Obtener por ID (ADMIN, ANALISTA, FORENSE)
                get("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val persona = personaService.obtenerPorId(id)
                        if (persona != null) {
                            call.respond(HttpStatusCode.OK, persona)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Persona no encontrada")
                        }
                    }
                }

                // Crear (ADMIN, FORENSE)
                post {
                    call.authorize(Roles.ADMIN, Roles.FORENSE) {
                        val dto = call.receive<PersonaDTO>()
                        val created = personaService.crear(dto)
                        created.onSuccess { nueva ->
                            call.respond(HttpStatusCode.Created, nueva)
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, "No se pudo crear: ${e.message}")
                        }
                    }
                }

                // Actualizar (ADMIN, FORENSE)
                put("{id}") {
                    call.authorize(Roles.ADMIN, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val dto = call.receive<PersonaDTO>()
                        val result = personaService.actualizar(id, dto)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Persona actualizada")
                        }.onFailure { e ->
                            call.respond(
                                if (e.message?.contains("no encontrado", ignoreCase = true) == true)
                                    HttpStatusCode.NotFound else HttpStatusCode.BadRequest,
                                e.message ?: "No se pudo actualizar"
                            )
                        }
                    }
                }

                // Eliminar (solo ADMIN)
                delete("{id}") {
                    call.authorize(Roles.ADMIN) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val result = personaService.eliminar(id)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Persona eliminada")
                        }.onFailure { e ->
                            call.respond(
                                if (e.message?.contains("no encontrado", ignoreCase = true) == true)
                                    HttpStatusCode.NotFound else HttpStatusCode.BadRequest,
                                e.message ?: "Persona no encontrada"
                            )
                        }
                    }
                }
            }
            route("/informacion_cadaver") {
                val informacionCadaverService = InformacionCadaverService()

                // Listar todos (ADMIN, ANALISTA)
                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA) {
                        val lista = informacionCadaverService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, lista)
                    }
                }

                // Obtener por ID (ADMIN, ANALISTA, FORENSE)
                get("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val info = informacionCadaverService.obtenerPorId(id)
                        if (info != null) {
                            call.respond(HttpStatusCode.OK, info)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Información de cadaver no encontrada")
                        }
                    }
                }

                // Crear (ADMIN, FORENSE)
                post {
                    call.authorize(Roles.ADMIN, Roles.FORENSE) {
                        val dto = call.receive<InformacionCadaverDTO>()
                        val created = informacionCadaverService.crear(dto)
                        created.onSuccess { nueva ->
                            call.respond(HttpStatusCode.Created, nueva)
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, "No se pudo crear: ${e.message}")
                        }
                    }
                }

                // Actualizar (ADMIN, FORENSE)
                put("{id}") {
                    call.authorize(Roles.ADMIN, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val dto = call.receive<InformacionCadaverDTO>()
                        val result = informacionCadaverService.actualizar(id, dto)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Información de cadaver actualizada")
                        }.onFailure { e ->
                            call.respond(
                                if (e.message?.contains("no encontrado", ignoreCase = true) == true)
                                    HttpStatusCode.NotFound else HttpStatusCode.BadRequest,
                                e.message ?: "No se pudo actualizar"
                            )
                        }
                    }
                }

                // Eliminar (solo ADMIN)
                delete("{id}") {
                    call.authorize(Roles.ADMIN) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val result = informacionCadaverService.eliminar(id)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Información de cadaver eliminada")
                        }.onFailure { e ->
                            call.respond(
                                if (e.message?.contains("no encontrado", ignoreCase = true) == true)
                                    HttpStatusCode.NotFound else HttpStatusCode.BadRequest,
                                e.message ?: "Información de cadaver no encontrada"
                            )
                        }
                    }
                }
            }
            route("/incidentes_clusters") {
                val incidenteClusterService = IncidenteClusterService()

                // Listar todos (ADMIN, ANALISTA)
                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA) {
                        val items = incidenteClusterService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, items)
                    }
                }

                // Obtener por ID (ADMIN, ANALISTA, FORENSE)
                get("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val item = incidenteClusterService.obtenerPorId(id)
                        if (item != null) {
                            call.respond(HttpStatusCode.OK, item)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "IncidenteCluster no encontrado")
                        }
                    }
                }

                // Crear (ADMIN, FORENSE)
                post {
                    call.authorize(Roles.ADMIN, Roles.FORENSE) {
                        val dto = call.receive<IncidenteClusterDTO>()
                        val created = incidenteClusterService.crear(dto)
                        created.onSuccess { nuevo ->
                            call.respond(HttpStatusCode.Created, nuevo)
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, "No se pudo crear: ${e.message}")
                        }
                    }
                }

                // Actualizar (ADMIN, FORENSE)
                put("{id}") {
                    call.authorize(Roles.ADMIN, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val dto = call.receive<IncidenteClusterDTO>()
                        val result = incidenteClusterService.actualizar(id, dto)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "IncidenteCluster actualizado")
                        }.onFailure { e ->
                            call.respond(
                                if (e.message?.contains("no encontrado", ignoreCase = true) == true)
                                    HttpStatusCode.NotFound else HttpStatusCode.BadRequest,
                                e.message ?: "No se pudo actualizar"
                            )
                        }
                    }
                }

                // Eliminar (solo ADMIN)
                delete("{id}") {
                    call.authorize(Roles.ADMIN) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val result = incidenteClusterService.eliminar(id)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "IncidenteCluster eliminado")
                        }.onFailure { e ->
                            call.respond(
                                if (e.message?.contains("no encontrado", ignoreCase = true) == true)
                                    HttpStatusCode.NotFound else HttpStatusCode.BadRequest,
                                e.message ?: "IncidenteCluster no encontrado"
                            )
                        }
                    }
                }
            }
            route("/envios_lab") {
                val envioLabService = EnvioLabService()

                // Listar todos (ADMIN, ANALISTA)
                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA) {
                        val envios = envioLabService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, envios)
                    }
                }

                // Obtener por ID (ADMIN, ANALISTA, FORENSE)
                get("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val envio = envioLabService.obtenerPorId(id)
                        if (envio != null) {
                            call.respond(HttpStatusCode.OK, envio)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Envío de laboratorio no encontrado")
                        }
                    }
                }

                // Crear (ADMIN, FORENSE)
                post {
                    call.authorize(Roles.ADMIN, Roles.FORENSE) {
                        val dto = call.receive<EnvioLabDTO>()
                        val created = envioLabService.crear(dto)
                        created.onSuccess { nuevo ->
                            call.respond(HttpStatusCode.Created, nuevo)
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, "No se pudo crear: ${e.message}")
                        }
                    }
                }

                // Actualizar (ADMIN, FORENSE)
                put("{id}") {
                    call.authorize(Roles.ADMIN, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val dto = call.receive<EnvioLabDTO>()
                        val result = envioLabService.actualizar(id, dto)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Envío de laboratorio actualizado")
                        }.onFailure { e ->
                            call.respond(
                                if (e.message?.contains("no encontrado", ignoreCase = true) == true)
                                    HttpStatusCode.NotFound else HttpStatusCode.BadRequest,
                                e.message ?: "No se pudo actualizar"
                            )
                        }
                    }
                }

                // Eliminar (solo ADMIN)
                delete("{id}") {
                    call.authorize(Roles.ADMIN) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val result = envioLabService.eliminar(id)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Envío de laboratorio eliminado")
                        }.onFailure { e ->
                            call.respond(
                                if (e.message?.contains("no encontrado", ignoreCase = true) == true)
                                    HttpStatusCode.NotFound else HttpStatusCode.BadRequest,
                                e.message ?: "Envío de laboratorio no encontrado"
                            )
                        }
                    }
                }
            }
            route("/evidencias_envios") {
                val evidenciaEnvioService = EvidenciaEnvioService()

                // Listar todos (ADMIN, ANALISTA)
                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA) {
                        val registros = evidenciaEnvioService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, registros)
                    }
                }

                // Obtener por ID (ADMIN, ANALISTA, FORENSE)
                get("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val registro = evidenciaEnvioService.obtenerPorId(id)
                        if (registro != null) {
                            call.respond(HttpStatusCode.OK, registro)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Registro no encontrado")
                        }
                    }
                }

                // Crear (ADMIN, FORENSE)
                post {
                    call.authorize(Roles.ADMIN, Roles.FORENSE) {
                        val dto = call.receive<EvidenciaEnvioDTO>()
                        val created = evidenciaEnvioService.crear(dto)
                        created.onSuccess { nuevo ->
                            call.respond(HttpStatusCode.Created, nuevo)
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, "No se pudo crear: ${e.message}")
                        }
                    }
                }

                // Actualizar (ADMIN, FORENSE)
                put("{id}") {
                    call.authorize(Roles.ADMIN, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val dto = call.receive<EvidenciaEnvioDTO>()
                        val result = evidenciaEnvioService.actualizar(id, dto)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Registro actualizado")
                        }.onFailure { e ->
                            call.respond(
                                if (e.message?.contains("no encontrado", ignoreCase = true) == true)
                                    HttpStatusCode.NotFound else HttpStatusCode.BadRequest,
                                e.message ?: "No se pudo actualizar"
                            )
                        }
                    }
                }

                // Eliminar (solo ADMIN)
                delete("{id}") {
                    call.authorize(Roles.ADMIN) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val result = evidenciaEnvioService.eliminar(id)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Registro eliminado")
                        }.onFailure { e ->
                            call.respond(
                                if (e.message?.contains("no encontrado", ignoreCase = true) == true)
                                    HttpStatusCode.NotFound else HttpStatusCode.BadRequest,
                                e.message ?: "Registro no encontrado"
                            )
                        }
                    }
                }
            }
            route("/clusters") {
                val clusterService = ClusterService()

                // Listar todos (ADMIN, ANALISTA)
                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA) {
                        val clusters = clusterService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, clusters)
                    }
                }

                // Obtener por ID (ADMIN, ANALISTA, FORENSE)
                get("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val cluster = clusterService.obtenerPorId(id)
                        if (cluster != null) {
                            call.respond(HttpStatusCode.OK, cluster)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Cluster no encontrado")
                        }
                    }
                }

                // Crear (ADMIN, ANALISTA)
                post {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA) {
                        val dto = call.receive<ClusterDTO>()
                        val created = clusterService.crear(dto)
                        created.onSuccess { nuevo ->
                            call.respond(HttpStatusCode.Created, nuevo)
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, "No se pudo crear: ${e.message}")
                        }
                    }
                }

                // Actualizar (ADMIN, ANALISTA)
                put("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val dto = call.receive<ClusterDTO>()
                        val result = clusterService.actualizar(id, dto)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Cluster actualizado")
                        }.onFailure { e ->
                            call.respond(
                                if (e.message?.contains("no encontrado", ignoreCase = true) == true)
                                    HttpStatusCode.NotFound else HttpStatusCode.BadRequest,
                                e.message ?: "No se pudo actualizar"
                            )
                        }
                    }
                }

                // Eliminar (solo ADMIN)
                delete("{id}") {
                    call.authorize(Roles.ADMIN) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val result = clusterService.eliminar(id)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Cluster eliminado")
                        }.onFailure { e ->
                            call.respond(
                                if (e.message?.contains("no encontrado", ignoreCase = true) == true)
                                    HttpStatusCode.NotFound else HttpStatusCode.BadRequest,
                                e.message ?: "Cluster no encontrado"
                            )
                        }
                    }
                }
            }
            route("/medios") {
                val medioService = MedioService()

                // Listar todos (ADMIN, ANALISTA)
                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA) {
                        val medios = medioService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, medios)
                    }
                }

                // Obtener por ID (ADMIN, ANALISTA, FORENSE)
                get("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val medio = medioService.obtenerPorId(id)
                        if (medio != null) {
                            call.respond(HttpStatusCode.OK, medio)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Medio no encontrado")
                        }
                    }
                }

                // Crear (ADMIN, FORENSE)
                post {
                    call.authorize(Roles.ADMIN, Roles.FORENSE) {
                        val dto = call.receive<MedioDTO>()
                        val created = medioService.crear(dto)
                        created.onSuccess { nuevo ->
                            call.respond(HttpStatusCode.Created, nuevo)
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, "No se pudo crear: ${e.message}")
                        }
                    }
                }

                // Actualizar (ADMIN, FORENSE)
                put("{id}") {
                    call.authorize(Roles.ADMIN, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val dto = call.receive<MedioDTO>()
                        val result = medioService.actualizar(id, dto)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Medio actualizado")
                        }.onFailure { e ->
                            call.respond(
                                if (e.message?.contains("no encontrado", ignoreCase = true) == true)
                                    HttpStatusCode.NotFound else HttpStatusCode.BadRequest,
                                e.message ?: "No se pudo actualizar"
                            )
                        }
                    }
                }

                // Eliminar (solo ADMIN)
                delete("{id}") {
                    call.authorize(Roles.ADMIN) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val result = medioService.eliminar(id)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Medio eliminado")
                        }.onFailure { e ->
                            call.respond(
                                if (e.message?.contains("no encontrado", ignoreCase = true) == true)
                                    HttpStatusCode.NotFound else HttpStatusCode.BadRequest,
                                e.message ?: "Medio no encontrado"
                            )
                        }
                    }
                }
            }
            route("/notificaciones") {
                val notificacionService = NotificacionService()

                // Listar notificaciones del usuario autenticado
                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val principal = call.principal<JWTPrincipal>()
                        val userId = try {
                            UUID.fromString(principal!!.getClaim("id", String::class))
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID de usuario inválido en token")
                            return@authorize
                        }

                        val notificaciones = notificacionService.obtenerPorUsuario(userId)
                        call.respond(HttpStatusCode.OK, notificaciones)
                    }
                }

                // Obtener por ID
                get("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val notificacion = notificacionService.obtenerPorId(id)
                        if (notificacion != null) {
                            call.respond(HttpStatusCode.OK, notificacion)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Notificación no encontrada")
                        }
                    }
                }

                // Crear
                post {
                    call.authorize(Roles.ADMIN, Roles.FORENSE) {
                        val dto = call.receive<NotificacionDTO>()
                        val created = notificacionService.crear(dto)
                        created.onSuccess { nueva ->
                            call.respond(HttpStatusCode.Created, nueva)
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, "No se pudo crear: ${e.message}")
                        }
                    }
                }

                // Actualizar
                put("{id}") {
                    call.authorize(Roles.ADMIN, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val dto = call.receive<NotificacionDTO>()
                        val result = notificacionService.actualizar(id, dto)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Notificación actualizada")
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, e.message ?: "No se pudo actualizar")
                        }
                    }
                }

                // Eliminar
                delete("{id}") {
                    call.authorize(Roles.ADMIN) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val result = notificacionService.eliminar(id)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Notificación eliminada")
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, e.message ?: "No se pudo eliminar")
                        }
                    }
                }
            }
            route("/reportes") {
                val reporteService = ReporteService()

                // Listar todos (ADMIN, ANALISTA)
                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA) {
                        val reportes = reporteService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, reportes)
                    }
                }

                // Obtener por ID (ADMIN, ANALISTA, FORENSE)
                get("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val reporte = reporteService.obtenerPorId(id)
                        if (reporte != null) {
                            call.respond(HttpStatusCode.OK, reporte)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Reporte no encontrado")
                        }
                    }
                }

                // Crear (ADMIN, ANALISTA)
                post {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA) {
                        val dto = call.receive<ReporteDTO>()
                        val created = reporteService.crear(dto)
                        created.onSuccess { nuevo ->
                            call.respond(HttpStatusCode.Created, nuevo)
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, "No se pudo crear: ${e.message}")
                        }
                    }
                }

                // Actualizar (ADMIN, ANALISTA)
                put("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val dto = call.receive<ReporteDTO>()
                        val result = reporteService.actualizar(id, dto)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Reporte actualizado")
                        }.onFailure { e ->
                            call.respond(
                                if (e.message?.contains("no encontrado", ignoreCase = true) == true)
                                    HttpStatusCode.NotFound else HttpStatusCode.BadRequest,
                                e.message ?: "No se pudo actualizar"
                            )
                        }
                    }
                }

                // Eliminar (solo ADMIN)
                delete("{id}") {
                    call.authorize(Roles.ADMIN) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val result = reporteService.eliminar(id)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Reporte eliminado")
                        }.onFailure { e ->
                            call.respond(
                                if (e.message?.contains("no encontrado", ignoreCase = true) == true)
                                    HttpStatusCode.NotFound else HttpStatusCode.BadRequest,
                                e.message ?: "Reporte no encontrado"
                            )
                        }
                    }
                }
            }
            route("/sesiones_usuario") {
                val sesionUsuarioService = SesionUsuarioService()

                // Listar todas (ADMIN, ANALISTA)
                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA) {
                        val sesiones = sesionUsuarioService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, sesiones)
                    }
                }

                // Obtener por ID (ADMIN, ANALISTA, FORENSE)
                get("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val sesion = sesionUsuarioService.obtenerPorId(id)
                        if (sesion != null) {
                            call.respond(HttpStatusCode.OK, sesion)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Sesión no encontrada")
                        }
                    }
                }

                // Crear sesión (ADMIN)
                post {
                    call.authorize(Roles.ADMIN) {
                        val dto = call.receive<SesionUsuarioDTO>()
                        val created = sesionUsuarioService.crear(dto)
                        created.onSuccess { nueva ->
                            call.respond(HttpStatusCode.Created, nueva)
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, "No se pudo crear: ${e.message}")
                        }
                    }
                }

                // Actualizar sesión (ADMIN)
                put("{id}") {
                    call.authorize(Roles.ADMIN) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val dto = call.receive<SesionUsuarioDTO>()
                        val result = sesionUsuarioService.actualizar(id, dto)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Sesión actualizada")
                        }.onFailure { e ->
                            call.respond(
                                if (e.message?.contains("no encontrado", ignoreCase = true) == true)
                                    HttpStatusCode.NotFound else HttpStatusCode.BadRequest,
                                e.message ?: "No se pudo actualizar"
                            )
                        }
                    }
                }

                // Eliminar sesión (solo ADMIN)
                delete("{id}") {
                    call.authorize(Roles.ADMIN) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val result = sesionUsuarioService.eliminar(id)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Sesión eliminada")
                        }.onFailure { e ->
                            call.respond(
                                if (e.message?.contains("no encontrado", ignoreCase = true) == true)
                                    HttpStatusCode.NotFound else HttpStatusCode.BadRequest,
                                e.message ?: "Sesión no encontrada"
                            )
                        }
                    }
                }
            }
            route("/tipos_evidencia") {
                val tipoEvidenciaService = TipoEvidenciaService()

                // Listar todos (ADMIN, ANALISTA, FORENSE)
                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val tipos = tipoEvidenciaService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, tipos)
                    }
                }

                // Obtener por ID (ADMIN, ANALISTA, FORENSE)
                get("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            idParam?.toInt()
                        } catch (_: Exception) {
                            null
                        }

                        if (id == null) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val tipo = tipoEvidenciaService.obtenerPorId(id)
                        if (tipo != null) {
                            call.respond(HttpStatusCode.OK, tipo)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Tipo de evidencia no encontrado")
                        }
                    }
                }
            }
            route("/tipos_incidente") {
                val tipoIncidenteService = TipoIncidenteService()

                // Listar todos (ADMIN, ANALISTA, FORENSE)
                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val tipos = tipoIncidenteService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, tipos)
                    }
                }

                // Obtener por ID (ADMIN, ANALISTA, FORENSE)
                get("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            idParam?.toInt()
                        } catch (_: Exception) {
                            null
                        }

                        if (id == null) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val tipo = tipoIncidenteService.obtenerPorId(id)
                        if (tipo != null) {
                            call.respond(HttpStatusCode.OK, tipo)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Tipo de incidente no encontrado")
                        }
                    }
                }
            }
            route("/cat_acciones_custodia") {
                val catAccionCustodiaService = CatAccionCustodiaService()

                // Listar todos (ADMIN, ANALISTA, FORENSE)
                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val acciones = catAccionCustodiaService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, acciones)
                    }
                }

                // Obtener por ID (ADMIN, ANALISTA, FORENSE)
                get("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            idParam?.toInt()
                        } catch (_: Exception) {
                            null
                        }

                        if (id == null) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val accion = catAccionCustodiaService.obtenerPorId(id)
                        if (accion != null) {
                            call.respond(HttpStatusCode.OK, accion)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Acción de custodia no encontrada")
                        }
                    }
                }
            }
            route("/cat_estados_caso") {
                val catEstadoCasoService = CatEstadoCasoService()

                // Listar todos (ADMIN, ANALISTA, FORENSE)
                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val estados = catEstadoCasoService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, estados)
                    }
                }

                // Obtener por ID (ADMIN, ANALISTA, FORENSE)
                get("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            idParam?.toInt()
                        } catch (_: Exception) {
                            null
                        }

                        if (id == null) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val estado = catEstadoCasoService.obtenerPorId(id)
                        if (estado != null) {
                            call.respond(HttpStatusCode.OK, estado)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Estado de caso no encontrado")
                        }
                    }
                }
            }
            route("/analisis_clustering") {
                val analisisClusteringService = AnalisisClusteringService()

                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA) {
                        val lista = analisisClusteringService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, lista)
                    }
                }

                get("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA) {
                        val id = call.parameters["id"]?.let { UUID.fromString(it) }
                            ?: return@authorize call.respond(HttpStatusCode.BadRequest, "ID inválido")

                        val analisis = analisisClusteringService.obtenerPorId(id)
                        if (analisis != null) {
                            call.respond(HttpStatusCode.OK, analisis)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Análisis no encontrado")
                        }
                    }
                }

                post {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA) {
                        val dto = call.receive<AnalisisClusteringDTO>()
                        analisisClusteringService.crear(dto).onSuccess {
                            call.respond(HttpStatusCode.Created, it)
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, e.message ?: "No se pudo crear el análisis")
                        }
                    }
                }

                put("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA) {
                        val id = call.parameters["id"]?.let { UUID.fromString(it) }
                            ?: return@authorize call.respond(HttpStatusCode.BadRequest, "ID inválido")

                        val dto = call.receive<AnalisisClusteringDTO>()
                        analisisClusteringService.actualizar(id, dto).onSuccess {
                            call.respond(HttpStatusCode.OK, "Análisis actualizado")
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, e.message ?: "No se pudo actualizar")
                        }
                    }
                }

                delete("{id}") {
                    call.authorize(Roles.ADMIN) {
                        val id = call.parameters["id"]?.let { UUID.fromString(it) }
                            ?: return@authorize call.respond(HttpStatusCode.BadRequest, "ID inválido")

                        analisisClusteringService.eliminar(id).onSuccess {
                            call.respond(HttpStatusCode.OK, "Análisis eliminado")
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, e.message ?: "No se pudo eliminar")
                        }
                    }
                }
            }
            route("/analisis_forenses") {
                val analisisForenseService = AnalisisForenseService()

                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA) {
                        val lista = analisisForenseService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, lista)
                    }
                }

                get("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val id = call.parameters["id"]?.let { UUID.fromString(it) }
                            ?: return@authorize call.respond(HttpStatusCode.BadRequest, "ID inválido")

                        val analisis = analisisForenseService.obtenerPorId(id)
                        if (analisis != null) {
                            call.respond(HttpStatusCode.OK, analisis)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Análisis forense no encontrado")
                        }
                    }
                }

                post {
                    call.authorize(Roles.ADMIN, Roles.FORENSE) {
                        val dto = call.receive<AnalisisForenseDTO>()
                        analisisForenseService.crear(dto).onSuccess {
                            call.respond(HttpStatusCode.Created, it)
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, e.message ?: "No se pudo crear")
                        }
                    }
                }

                put("{id}") {
                    call.authorize(Roles.ADMIN, Roles.FORENSE) {
                        val id = call.parameters["id"]?.let { UUID.fromString(it) }
                            ?: return@authorize call.respond(HttpStatusCode.BadRequest, "ID inválido")

                        val dto = call.receive<AnalisisForenseDTO>()
                        analisisForenseService.actualizar(id, dto).onSuccess {
                            call.respond(HttpStatusCode.OK, "Análisis forense actualizado")
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, e.message ?: "No se pudo actualizar")
                        }
                    }
                }

                delete("{id}") {
                    call.authorize(Roles.ADMIN) {
                        val id = call.parameters["id"]?.let { UUID.fromString(it) }
                            ?: return@authorize call.respond(HttpStatusCode.BadRequest, "ID inválido")

                        analisisForenseService.eliminar(id).onSuccess {
                            call.respond(HttpStatusCode.OK, "Análisis forense eliminado")
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, e.message ?: "No se pudo eliminar")
                        }
                    }
                }
            }
            route("/causas_muerte") {
                val causaMuerteService = CausaMuerteService()

                // Listar todas
                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val lista = causaMuerteService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, lista)
                    }
                }

                // Obtener por ID
                get("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val id = call.parameters["id"]?.toIntOrNull()
                            ?: return@authorize call.respond(HttpStatusCode.BadRequest, "ID inválido")

                        val causa = causaMuerteService.obtenerPorId(id)
                        if (causa != null) {
                            call.respond(HttpStatusCode.OK, causa)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Causa de muerte no encontrada")
                        }
                    }
                }
            }
            route("/caso_estados") {
                val casoEstadoService = CasoEstadoService()

                // Listar todos (ADMIN, ANALISTA)
                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA) {
                        val lista = casoEstadoService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, lista)
                    }
                }

                // Obtener por ID (ADMIN, ANALISTA, FORENSE)
                get("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            return@authorize call.respond(HttpStatusCode.BadRequest, "ID inválido")
                        }

                        val estado = casoEstadoService.obtenerPorId(id)
                        if (estado != null) {
                            call.respond(HttpStatusCode.OK, estado)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Estado de caso no encontrado")
                        }
                    }
                }

                // Crear (solo desde la app - ADMIN o FORENSE)
                post {
                    call.authorize(Roles.ADMIN, Roles.FORENSE) {
                        val dto = call.receive<CasoEstadoDTO>()
                        val created = casoEstadoService.crear(dto)
                        created.onSuccess { nuevo ->
                            call.respond(HttpStatusCode.Created, nuevo)
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, "No se pudo crear: ${e.message}")
                        }
                    }
                }
            }
            route("/estados_descomposicion") {
                val estadoDescomposicionService = EstadoDescomposicionService()

                // Listar todos (ADMIN, ANALISTA, FORENSE)
                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val estados = estadoDescomposicionService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, estados)
                    }
                }

                // Obtener por ID (ADMIN, ANALISTA, FORENSE)
                get("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            idParam?.toInt()
                        } catch (_: Exception) {
                            null
                        }

                        if (id == null) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val estado = estadoDescomposicionService.obtenerPorId(id)
                        if (estado != null) {
                            call.respond(HttpStatusCode.OK, estado)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Estado de descomposición no encontrado")
                        }
                    }
                }
            }
            route("/logs_auditoria") {
                val logAuditoriaService = LogAuditoriaService()

                // Listar todos (ADMIN, ANALISTA)
                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA) {
                        val logs = logAuditoriaService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, logs)
                    }
                }

                // Obtener por ID (ADMIN, ANALISTA)
                get("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            null
                        }

                        if (id == null) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val log = logAuditoriaService.obtenerPorId(id)
                        if (log != null) {
                            call.respond(HttpStatusCode.OK, log)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Log de auditoría no encontrado")
                        }
                    }
                }
            }
            route("/mecanismos_muerte") {
                val mecanismoMuerteService = MecanismoMuerteService()

                // Listar todos (ADMIN, ANALISTA, FORENSE)
                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val mecanismos = mecanismoMuerteService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, mecanismos)
                    }
                }

                // Obtener por ID (ADMIN, ANALISTA, FORENSE)
                get("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            idParam?.toInt()
                        } catch (_: Exception) {
                            null
                        }

                        if (id == null) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val mecanismo = mecanismoMuerteService.obtenerPorId(id)
                        if (mecanismo != null) {
                            call.respond(HttpStatusCode.OK, mecanismo)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Mecanismo de muerte no encontrado")
                        }
                    }
                }
            }
            route("/pruebas_lab") {
                val pruebaLabService = PruebaLabService()

                // Listar todos (ADMIN, ANALISTA, FORENSE)
                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val pruebas = pruebaLabService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, pruebas)
                    }
                }

                // Obtener por ID (ADMIN, ANALISTA, FORENSE)
                get("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val prueba = pruebaLabService.obtenerPorId(id)
                        if (prueba != null) {
                            call.respond(HttpStatusCode.OK, prueba)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Prueba de laboratorio no encontrada")
                        }
                    }
                }

                // Crear (ADMIN, FORENSE)
                post {
                    call.authorize(Roles.ADMIN, Roles.FORENSE) {
                        val dto = call.receive<PruebaLabDTO>()
                        val created = pruebaLabService.crear(dto)
                        created.onSuccess { nueva ->
                            call.respond(HttpStatusCode.Created, nueva)
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, "No se pudo crear: ${e.message}")
                        }
                    }
                }

                // Actualizar (ADMIN, FORENSE)
                put("{id}") {
                    call.authorize(Roles.ADMIN, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val dto = call.receive<PruebaLabDTO>()
                        val result = pruebaLabService.actualizar(id, dto)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Prueba de laboratorio actualizada")
                        }.onFailure { e ->
                            call.respond(
                                if (e.message?.contains("no encontrada", ignoreCase = true) == true)
                                    HttpStatusCode.NotFound else HttpStatusCode.BadRequest,
                                e.message ?: "No se pudo actualizar"
                            )
                        }
                    }
                }

                // Eliminar (solo ADMIN)
                delete("{id}") {
                    call.authorize(Roles.ADMIN) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val result = pruebaLabService.eliminar(id)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Prueba de laboratorio eliminada")
                        }.onFailure { e ->
                            call.respond(
                                if (e.message?.contains("no encontrada", ignoreCase = true) == true)
                                    HttpStatusCode.NotFound else HttpStatusCode.BadRequest,
                                e.message ?: "Prueba no encontrada"
                            )
                        }
                    }
                }
            }
            route("/personas_casos") {
                val personaCasoService = PersonaCasoService()

                // Listar todos (ADMIN, ANALISTA, FORENSE)
                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val relaciones = personaCasoService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, relaciones)
                    }
                }

                // Obtener por ID (ADMIN, ANALISTA, FORENSE)
                get("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val relacion = personaCasoService.obtenerPorId(id)
                        if (relacion != null) {
                            call.respond(HttpStatusCode.OK, relacion)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Relación persona-caso no encontrada")
                        }
                    }
                }

                // Crear (ADMIN, FORENSE)
                post {
                    call.authorize(Roles.ADMIN, Roles.FORENSE) {
                        val dto = call.receive<PersonaCasoDTO>()
                        val created = personaCasoService.crear(dto)
                        created.onSuccess { nueva ->
                            call.respond(HttpStatusCode.Created, nueva)
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, "No se pudo crear: ${e.message}")
                        }
                    }
                }

                // Actualizar (ADMIN, FORENSE)
                put("{id}") {
                    call.authorize(Roles.ADMIN, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val dto = call.receive<PersonaCasoDTO>()
                        val result = personaCasoService.actualizar(id, dto)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Relación actualizada")
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, e.message ?: "No se pudo actualizar")
                        }
                    }
                }

                // Eliminar (solo ADMIN)
                delete("{id}") {
                    call.authorize(Roles.ADMIN) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val result = personaCasoService.eliminar(id)
                        result.onSuccess {
                            call.respond(HttpStatusCode.OK, "Relación eliminada")
                        }.onFailure { e ->
                            call.respond(HttpStatusCode.BadRequest, e.message ?: "No se pudo eliminar")
                        }
                    }
                }
            }
            route("/vista_cadena_custodia") {
                val vistaService = VistaCadenaCustodiaService()

                // Listar todos (ADMIN, ANALISTA, FORENSE)
                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val lista = vistaService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, lista)
                    }
                }

                // Obtener por ID (ADMIN, ANALISTA, FORENSE)
                get("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val registro = vistaService.obtenerPorId(id)
                        if (registro != null) {
                            call.respond(HttpStatusCode.OK, registro)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "No encontrado")
                        }
                    }
                }
            }
            route("/vista_casos_completa") {
                val vistaCasosCompletaService = VistaCasosCompletaService()

                // Listar todos (ADMIN, ANALISTA, FORENSE)
                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val casos = vistaCasosCompletaService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, casos)
                    }
                }

                // Obtener por ID (ADMIN, ANALISTA, FORENSE)
                get("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            null
                        }

                        if (id == null) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val caso = vistaCasosCompletaService.obtenerPorId(id)
                        if (caso != null) {
                            call.respond(HttpStatusCode.OK, caso)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Caso no encontrado en vista completa")
                        }
                    }
                }
            }
            route("/vista_incidentes_cadaver") {
                val vistaIncidentesCadaverService = VistaIncidentesCadaverService()

                // Listar todos (ADMIN, ANALISTA, FORENSE)
                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val incidentes = vistaIncidentesCadaverService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, incidentes)
                    }
                }

                // Obtener por ID (ADMIN, ANALISTA, FORENSE)
                get("{id}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val idParam = call.parameters["id"]
                        val id = try {
                            UUID.fromString(idParam)
                        } catch (_: Exception) {
                            null
                        }

                        if (id == null) {
                            call.respond(HttpStatusCode.BadRequest, "ID inválido")
                            return@authorize
                        }

                        val incidente = vistaIncidentesCadaverService.obtenerPorId(id)
                        if (incidente != null) {
                            call.respond(HttpStatusCode.OK, incidente)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Incidente no encontrado en vista cadaver")
                        }
                    }
                }
            }
            route("/vw_caso_historial") {
                val vwCasoHistorialService = VwCasoHistorialService()

                // Listar todos (ADMIN, ANALISTA)
                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA) {
                        val historial = vwCasoHistorialService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, historial)
                    }
                }

                // Listar por casoId (ADMIN, ANALISTA, FORENSE)
                get("{casoId}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val casoParam = call.parameters["casoId"]
                        val casoId = try {
                            UUID.fromString(casoParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "casoId inválido")
                            return@authorize
                        }

                        val historial = vwCasoHistorialService.obtenerPorCaso(casoId)
                        if (historial.isNotEmpty()) {
                            call.respond(HttpStatusCode.OK, historial)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "No hay historial para este caso")
                        }
                    }
                }
            }
            route("/vw_caso_ultimo_estado") {
                val vwCasoUltimoEstadoService = VwCasoUltimoEstadoService()

                // Listar todos (ADMIN, ANALISTA)
                get {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA) {
                        val estados = vwCasoUltimoEstadoService.obtenerTodos()
                        call.respond(HttpStatusCode.OK, estados)
                    }
                }

                // Obtener por casoId (ADMIN, ANALISTA, FORENSE)
                get("{casoId}") {
                    call.authorize(Roles.ADMIN, Roles.ANALISTA, Roles.FORENSE) {
                        val casoParam = call.parameters["casoId"]
                        val casoId = try {
                            UUID.fromString(casoParam)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "casoId inválido")
                            return@authorize
                        }

                        val estado = vwCasoUltimoEstadoService.obtenerPorCaso(casoId)
                        if (estado != null) {
                            call.respond(HttpStatusCode.OK, estado)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "No se encontró el último estado del caso")
                        }
                    }
                }
            }
        }
    }
}
