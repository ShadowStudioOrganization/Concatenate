package org.shadow.studio.concatenate.backend.download

import io.ktor.client.*
import org.shadow.studio.concatenate.backend.util.globalClient

class AssetDownloader(
    poolSize: Int = 64,
    taskTTL: Int = 7,
    ktorClient: HttpClient = globalClient,
    ktorBuffetSize: Long = 256 * 1024L
) : ConcatenateDownloader(poolSize, taskTTL, ktorClient, ktorBuffetSize) {
    // https://resources.download.minecraft.net/<hash的前两位字符>/<hash>



}