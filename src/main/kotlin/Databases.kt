package cadmap.backend

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import java.lang.Thread.sleep
import java.sql.SQLTransientConnectionException

object Databases {
    private var initialized = false

    fun init() {
        if (initialized) return

        val url = System.getenv("DB_URL")
            ?: "jdbc:postgresql://db.tyfaanzlmavcddvethku.supabase.co:5432/postgres?sslmode=require"
        val user = System.getenv("DB_USER") ?: "postgres"
        val password = System.getenv("DB_PASSWORD") ?: "postgres"

        println("🛰 [DB] Intentando conexión a Supabase...")
        println("🧩 [DB_URL]: $url")
        println("👤 [DB_USER]: $user")
        println("🔒 [DB_PASSWORD length]: ${password.length}")

        val config = HikariConfig().apply {
            jdbcUrl = url
            username = user
            this.password = password
            driverClassName = "org.postgresql.Driver"
            maximumPoolSize = 8           // equilibrio entre concurrencia y estabilidad
            minimumIdle = 2               // conexiones mínimas listas
            idleTimeout = 60_000          // 1 min antes de reciclar conexiones inactivas
            maxLifetime = 300_000         // 5 min antes de renovar conexiones viejas
            connectionTimeout = 10_000    // tiempo máximo para obtener conexión (10s)
            leakDetectionThreshold = 10_000 // detectar fugas si conexión dura >10s

            // Seguridad y consistencia
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_READ_COMMITTED"
            addDataSourceProperty("sslmode", "require")
            addDataSourceProperty("tcpKeepAlive", true)
        }

        var attempt = 1
        val maxAttempts = 3
        var connected = false

        while (attempt <= maxAttempts && !connected) {
            try {
                println("🔁 [DB] Intento de conexión $attempt de $maxAttempts...")

                val dataSource = HikariDataSource(config)
                Database.connect(dataSource)

                // ✅ Verificación de conexión (sin abrir sesión extra)
                transaction {
                    exec("SELECT 1") { rs ->
                        if (rs.next()) println("🎯 [DB] Conexión probada exitosamente (SELECT 1 OK)")
                    }
                }

                connected = true
                initialized = true
                println("✅ [DB] Conexión inicializada correctamente con HikariCP (pool máx: ${config.maximumPoolSize})")

            } catch (e: SQLTransientConnectionException) {
                println("⚠ [DB] No hay conexiones disponibles o el servidor rechazó nuevas.")
                if (attempt < maxAttempts) {
                    println("⏳ [DB] Reintentando en 5 segundos...")
                    sleep(5000)
                } else {
                    println("🚨 [DB] Pool agotado después de $maxAttempts intentos.")
                    throw RuntimeException("El pool de conexiones está agotado.", e)
                }

            } catch (e: Exception) {
                println("❌ [DB] Error al conectar (intento $attempt): ${e.message}")
                if (attempt < maxAttempts) {
                    println("⏳ [DB] Reintentando en 5 segundos...")
                    sleep(5000)
                } else {
                    println("🚨 [DB] Fallaron todos los intentos de conexión. Abortando arranque.")
                    throw RuntimeException("No se pudo conectar a la base de datos después de $maxAttempts intentos.", e)
                }
            }

            attempt++
        }
    }
}

fun Application.configureDatabases() {
    Databases.init()
}