package cadmap.backend

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

object Databases {
    fun init() {
        val url = System.getenv("DB_URL")
            ?: "jdbc:postgresql://localhost:5432/postgres"  // fallback local
        val user = System.getenv("DB_USER") ?: "postgres"
        val password = System.getenv("DB_PASSWORD") ?: "postgres"

        Database.connect(
            url = url,
            driver = "org.postgresql.Driver",
            user = user,
            password = password
        )
    }
}

fun Application.configureDatabases() {
    Databases.init()
}