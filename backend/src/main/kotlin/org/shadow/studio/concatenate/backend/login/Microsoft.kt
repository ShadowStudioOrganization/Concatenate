package org.shadow.studio.concatenate.backend.login

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import org.shadow.studio.concatenate.backend.util.parseJsonToMap

fun getAccessToken(accessTokenMic: String) = runBlocking {
    val client = HttpClient(OkHttp) {
        engine {
            config {
                followRedirects(true)
            }
        }
    }

    val res1 = client.post("https://user.auth.xboxlive.com/user/authenticate") {
        val body = """
                {
                    "Properties": {
                        "AuthMethod": "RPS",
                        "SiteName": "user.auth.xboxlive.com",
                        "RpsTicket": "$accessTokenMic"
                    },
                    "RelyingParty": "http://auth.xboxlive.com",
                    "TokenType": "JWT"
                 }
            """.trimIndent()
        setBody(body)
        headers {
            append("Content-Type", "application/x-www-form-urlencoded")
        }
    }
    val res1Json = parseJsonToMap(res1.bodyAsText())
    val TokenXbox = res1Json.get("Token")
    val uerHash = res1Json.get("DisplayClaims").toString().split("uhs=")[1].split("}]}")[0]

    val res2 = client.post("https://xsts.auth.xboxlive.com/xsts/authorize") {
        val body = """
                {
                                    "Properties": {
                                        "SandboxId": "RETAIL",
                                        "UserTokens": [
                                            "$TokenXbox"
                                        ]
                                    },
                                    "RelyingParty": "rp://api.minecraftservices.com/",
                                    "TokenType": "JWT"
                                 }
            """.trimIndent()
        setBody(body)
        headers {
            append("Content-Type", "application/x-www-form-urlencoded")
        }
    }
    val TokenXSTS = parseJsonToMap(res2.bodyAsText()).get("Token")

    val res3 = client.post("https://api.minecraftservices.com/authentication/login_with_xbox") {
        val body = """
                {
                    "identityToken": "s=$uerHash;$TokenXSTS"
                 }
            """.trimIndent()
        setBody(body)
        headers {
            append("Content-Type", "application/json")
        }
    }

    val access_token = parseJsonToMap(res3.bodyAsText()).get("access_token")
    println(access_token)
}