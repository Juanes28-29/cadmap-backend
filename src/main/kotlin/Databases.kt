package cadmap.backend

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

object Databases {
    fun init() {
        val url = System.getenv("DB_URL")
            ?: "jdbc:postgresql://localhost:5432/postgres"
        val user = System.getenv("DB_USER") ?: "postgres"
        val password = System.getenv("DB_PASSWORD") ?: "postgres"

        println("DEBUG -> DB_URL=$url")
        println("DEBUG -> DB_USER=$user")
        println("DEBUG -> DB_PASSWORD length=${password.length}")

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