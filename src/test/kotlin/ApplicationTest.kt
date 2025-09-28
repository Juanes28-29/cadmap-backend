package cadmap.backend

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import io.ktor.client.statement.*
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun testEstado() = testApplication {
        application {
            module()
        }
        client.get("/estado").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("CadMap backend activo", bodyAsText())
        }
    }

}