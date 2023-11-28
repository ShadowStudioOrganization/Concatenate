package org.shadow.studio.concatenate.backend.download

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.shadow.studio.concatenate.backend.login.OfflineMethod
import org.shadow.studio.concatenate.backend.resolveBackendBuildPath
import org.shadow.studio.concatenate.backend.util.getInternalLauncherMetaManifest
import org.shadow.studio.concatenate.backend.util.globalLogger
import java.io.BufferedReader
import java.io.InputStreamReader

internal class MinecraftClientDownloadManagerTest {

    @Test
    fun downloadTest(): Unit = runBlocking {
        val manager = MinecraftClientDownloadManager("1.20.1", "乐", resolveBackendBuildPath("run"), getInternalLauncherMetaManifest())
        manager.downloadManifest()
        manager.initResourcesDownloader()
        manager.download {
            this.useRepository("mcbbs")
        }

        val launcher = manager.buildLauncher {
            loginMethod = OfflineMethod("G_Breeze")
        }
        val instance = launcher.launch()
        val inputStream = instance.process.inputStream

        val reader = BufferedReader(InputStreamReader(inputStream))
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            println(line)
        }

        val errorStream = instance.process.errorStream

        val errReader = BufferedReader(InputStreamReader(errorStream))
        var errLine: String?
        while (errReader.readLine().also { errLine = it } != null) {
            println(errLine)
        }

        val exitCode = instance.process.waitFor()
        globalLogger.info("Minecraft process exited with code: $exitCode")
    }
}