package cadmap.backend

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import java.lang.Thread.sleep
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLTransientConnectionException

object Databases {
    private var initialized = false

    fun init() {
        if (initialized) return

        val url = System.getenv("DB_URL")
            ?: "jdbc:postgresql://aws-1-us-east-1.pooler.supabase.com:6543/postgres?sslmode=require"
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

            // Supabase free = muy pocos slots simultáneos (máximo 1)
            maximumPoolSize = 1
            minimumIdle = 0
            idleTimeout = 10_000
            maxLifetime = 30_000
            connectionTimeout = 15_000

            isAutoCommit = false
            transactionIsolation = "TRANSACTION_READ_COMMITTED"

            addDataSourceProperty("sslmode", "require")
        }

        var attempt = 1
        val maxAttempts = 3
        var connected = false

        while (attempt <= maxAttempts && !connected) {
            try {
                println("🔁 [DB] Intento de conexión $attempt de $maxAttempts...")

                // Configurar pool
                val dataSource = HikariDataSource(config)
                Database.connect(dataSource)

                // Verificación directa (sin Exposed)
                DriverManager.getConnection(url, user, password).use { conn: Connection ->
                    val dbName = conn.metaData.databaseProductName
                    val version = conn.metaData.databaseProductVersion
                    println("🎯 [DB] Conexión exitosa a: $dbName $version")
                }

                connected = true
                initialized = true
                println("✅ [DB] Conexión inicializada correctamente con HikariCP (pool máx: ${config.maximumPoolSize})")

            } catch (e: SQLTransientConnectionException) {
                println("⚠ [DB] No hay conexiones disponibles en el pool o el servidor rechazó nuevas conexiones.")
                println("💡 Posible causa: límite de conexiones alcanzado en Supabase (plan gratuito = 10 máx).")
                if (attempt < maxAttempts) {
                    println("⏳ [DB] Reintentando en 5 segundos...")
                    sleep(5000)
                } else {
                    println("🚨 [DB] Fallaron todos los intentos. No hay conexiones disponibles.")
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