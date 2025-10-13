package cadmap.backend

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

object Databases {
    private var initialized = false

    fun init() {
        if (initialized) return

        // --- Variables de entorno ---
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

            maximumPoolSize = 1
            minimumIdle = 0
            idleTimeout = 10_000
            maxLifetime = 30_000
            connectionTimeout = 15_000

            isAutoCommit = false
            transactionIsolation = "TRANSACTION_READ_COMMITTED"

            addDataSourceProperty("sslmode", "require")
        }

        try {
            val dataSource = HikariDataSource(config)
            Database.connect(dataSource)
            initialized = true
            println("[DB] Conexión inicializada correctamente con HikariCP (pool máx: ${config.maximumPoolSize})")
        } catch (e: Exception) {
            println("[DB] Error al inicializar conexión: ${e.message}")
            e.printStackTrace()
        }
    }
}

// --- Inicialización desde Application.kt ---
fun Application.configureDatabases() {
    Databases.init()
}