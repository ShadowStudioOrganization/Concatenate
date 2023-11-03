package org.shadow.studio.concatenate.backend.download

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.File


object DownloadUtil {

    val cache: File = File("")

    private val defaultClient = HttpClient(OkHttp) {
        engine {
            config {
                followRedirects(true)
            }
        }

        BrowserUserAgent()
    }

    suspend fun getAsStream(url: String, threads: Int): HttpResponse {
        return withContext(Dispatchers.IO) {
            val jobs = async {
                val fileName = url.substringAfterLast("/")
                defaultClient.get(url)
            }
            jobs.await()
        }
    }

    suspend fun getAsStream(urls: List<String>, threads: Int = urls.size) {

        withContext(Dispatchers.IO) {
            val jobs = urls.map { url ->
                async {
                    val fileName = url.substringAfterLast("/")
                    val k = defaultClient.get(url)
                }
            }


        }


    }
}