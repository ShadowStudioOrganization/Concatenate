package org.shadow.studio.concatenate.backend.util

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

fun uncompressZipFile(zipFile: File, outputDir: File, cover: Boolean = false, filter: ((ZipEntry) -> Boolean)? = null) {
    val buffer = ByteArray(1024 * 50)
    ZipInputStream(FileInputStream(zipFile)).use { zis ->
        var entry: ZipEntry? = zis.nextEntry

        while (entry != null) {
            if (filter != null && !filter(entry)) {
                entry = zis.nextEntry
                continue
            }

            val newFile = File(outputDir, entry.name)

            if (newFile.exists() && !cover) {
                entry = zis.nextEntry
                continue
            }

            if (entry.isDirectory) newFile.mkdirs() else {
                val outputStream = FileOutputStream(newFile)
                var length: Int
                while (zis.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
                outputStream.close()
            }

            entry = zis.nextEntry
        }
    }
}
