package org.shadow.studio.concatenate.backend.data.download

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.nio.file.Path

class DownloadTaskTest {

    @Test
    fun getDownloadUrl() {
        val url1 = "url1"
        val url2 = "url2"
        val url3 = "url3"
        val url4 = "url4"

        val task = DownloadTask(0L..1L, RemoteFile(url1, 1L, Path.of("")))

        assertEquals(url1, task.getDownloadUrl())

        task.apply {
            remoteFile.downloadSources = listOf(url2, url3, url4)
        }

        assertEquals(url1, task.getDownloadUrl())

        task.isFailedOnFirstTime = true
        assertEquals(url2, task.getDownloadUrl())

        task.tweakDownloadIndex()
        assertEquals(url3, task.getDownloadUrl())

        task.tweakDownloadIndex()
        assertEquals(url4, task.getDownloadUrl())

        task.tweakDownloadIndex()
        assertEquals(url2, task.getDownloadUrl())
    }
}