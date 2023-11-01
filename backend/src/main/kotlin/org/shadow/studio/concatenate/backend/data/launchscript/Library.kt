package org.shadow.studio.concatenate.backend.data.launchscript

data class Library(
    val downloads: DownloadsX,
    val name: String,
    val rules: List<Rule>,
    val url: String
)