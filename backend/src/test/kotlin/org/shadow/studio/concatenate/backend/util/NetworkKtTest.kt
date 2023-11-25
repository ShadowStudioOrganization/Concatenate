package org.shadow.studio.concatenate.backend.util

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.shadow.studio.concatenate.backend.data.download.RemoteFile
import org.shadow.studio.concatenate.backend.showRunTime
import java.io.RandomAccessFile
import java.nio.file.Path

class NetworkKtTest {

    @Test
    fun testKtorRangedDownloadAndTransferTo(): Unit = showRunTime {
        val rf = RemoteFile(
            "https://libraries.minecraft.net/org/lwjgl/lwjgl-glfw/3.3.1/lwjgl-glfw-3.3.1-natives-windows-x86.jar",
            139251,
            Path.of("D:/ProjectFiles/idea/Concatenate/backend/build/tmp/a.jar"),
            "b997e3391d6ce8f05487e7335d95c606043884a1"
        )

//        println(globalClient.get(rf.url))

//        globalClient.prepareGet(rf.url).execute {
//            println(it)
//            val channel: ByteReadChannel = it.bodyAsChannel()
//
//            val file = RandomAccessFile(rf.localDestination.toFile(), "rw")
//        }

        ktorRangedDownloadAndTransferTo(
            rf.url,
            0 until rf.size,
            rf.localDestination,
            rf.size,
            client = HttpClient(OkHttp) {
                engine {
                    proxy = ProxyBuilder.http("http://127.0.0.1:7890")
                }
            },
            callback = { it, _, _ ->
                println(">>$it")
            })
    }
}