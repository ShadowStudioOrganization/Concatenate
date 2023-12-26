package org.shadow.studio.concatenate.backend.util

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

suspend fun cursePackages() {
    val client = HttpClient(OkHttp) {
        engine {
            config {
                followRedirects(true)
            }
        }
    }

    val res = client.get("https://www.curseforge.com/minecraft/search?class=modpacks")
    println(res)
    val result = res.bodyAsText()
    println(result)
}
