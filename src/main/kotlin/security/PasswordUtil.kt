package cadmap.backend.security

import org.mindrot.jbcrypt.BCrypt

object PasswordUtil {
    fun hashPassword(plain: String): String {
        return BCrypt.hashpw(plain, BCrypt.gensalt(12))
    }

    fun verifyPassword(plain: String, hashed: String): Boolean {
        return BCrypt.checkpw(plain, hashed)
    }
}