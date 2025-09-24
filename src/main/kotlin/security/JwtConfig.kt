package cadmap.backend.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.JWTVerifier
import java.util.*

object JwtConfig {
    private val secret = System.getenv("JWT_SECRET") ?: "super-secreto"
    private const val issuer = "cadmap"
    private const val audience = "cadmap_audience"
    private const val validityInMs = 3_600_000

    private val algorithm = Algorithm.HMAC256(secret)

    val verifier: JWTVerifier = JWT.require(algorithm)
        .withIssuer(issuer)
        .withAudience(audience)
        .build()

    fun generateToken(email: String, username: String, rol: String): String =
        JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("email", email)
            .withClaim("username", username)
            .withClaim("rol", rol)
            .withExpiresAt(Date(System.currentTimeMillis() + validityInMs))
            .sign(algorithm)
}