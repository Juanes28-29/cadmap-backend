package cadmap.backend

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

object Databases {
    fun init() {
        Database.connect(
            url = "jdbc:postgresql://db.tyfaanzlmavcddvethku.supabase.co:5432/postgres?sslmode=require",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "cadmap20252"
        )
    }
}
fun Application.configureDatabases() {
    Databases.init()
}
