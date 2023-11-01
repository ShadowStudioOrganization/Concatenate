package org.shadow.studio.concatenate.backend.data.launchscript

data class AssetIndex(
    val id: String,
    val sha1: String,
    val size: Int,
    val totalSize: Int,
    val url: String
)