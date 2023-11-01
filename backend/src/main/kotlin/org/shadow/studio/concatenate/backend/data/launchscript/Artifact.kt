package org.shadow.studio.concatenate.backend.data.launchscript

data class Artifact(
    val path: String,
    val sha1: String,
    val size: Int,
    val url: String
)