package org.shadow.studio.concatenate.backend.login

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.shadow.studio.concatenate.backend.util.getUUIDFromPlayerName
import org.shadow.studio.concatenate.backend.util.globalLogger
import java.util.*

interface LoginMethod {

    suspend fun login(): LoginInfo

    fun insertJvmArguments(): List<String> {
        return emptyList()
    }
}

data class LoginInfo (
    val authPlayerName: String,
    val authUUID: String,
    val authAccessToken: String,
    val authXUID: String,
    val userProperties: String,
    val authSession: String,
)

class OfflineMethod(private val playerName: String) : LoginMethod {
    override suspend fun login(): LoginInfo =
        LoginInfo(
            playerName,
            playerName.let { name ->
                runCatching { getUUIDFromPlayerName(name) }
                    .onFailure { globalLogger.error(it.message) }
                    .getOrElse {
                        val seed = name.hashCode().toLong()
                        val random = Random(seed)
                        val uuid = UUID(random.nextLong(), random.nextLong())
                        uuid.toString().replace("-", "").lowercase()
                    }
            },
            "concatenate_token_bingo_bingo_now",
            "\${auth_xuid}",
            "{}",
            "1"
        )
}