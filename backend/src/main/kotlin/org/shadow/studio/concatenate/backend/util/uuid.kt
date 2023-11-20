package org.shadow.studio.concatenate.backend.util

import io.ktor.client.request.*
import io.ktor.client.statement.*

suspend fun getUUIDFromPlayerName(name: String): String {
    val response = globalClient.get("https://api.mojang.com/users/profiles/minecraft/$name")
    val body = parseJson(response.bodyAsText())

    if (body.has("errorMessage"))
        throw PlayerUUIDNotFoundException(body.get("errorMessage").textValue())

    if (!body.has("id")) error("weird, it should have 'id' field, but actual: $body")

    return body.get("id").textValue()
}

class PlayerUUIDNotFoundException(message: String) : Exception(message)