package org.shadow.studio.concatenate.backend.data.profile

interface LibraryItem {
    val name: String
}

class CommonLibraryItem(
    override val name: String,
    val downloads: Downloads
) : LibraryItem

class FabricLibraryItem(override val name: String, val url: String) : LibraryItem

class Downloads(val artifact: Artifact)

class Artifact(
    val path: String,
    val sha1: String,
    val size: Long,
    val url: String
)