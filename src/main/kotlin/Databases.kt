package cadmap.backend

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

object Databases {
    private var initialized = false

    fun init() {
        if (initialized) return

        val url = System.getenv("DB_URL")
            ?: "jdbc:postgresql://aws-1-us-east-1.supabase.com:5432/postgres"
        val user = System.getenv("DB_USER") ?: "postgres"
        val password = System.getenv("DB_PASSWORD") ?: "postgres"

        println("DEBUG - DB_URL: $url")
        println("DEBUG - DB_USER: $user")
        println("DEBUG - DB_PASSWORD length=${password.length}")

        val config = HikariConfig().apply {
            jdbcUrl = url
            username = user
            this.password = password
            driverClassName = "org.postgresql.Driver"

            maximumPoolSize = 5
            minimumIdle = 1
            idleTimeout = 10000
            maxLifetime = 30000
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"

            addDataSourceProperty("sslmode", "require")
        }

        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)

        initialized = true
        println("Conexión a base de datos inicializada con HikariCP (pool máx: ${config.maximumPoolSize})")
    }
}

fun Application.configureDatabases() {
    Databases.init()
}