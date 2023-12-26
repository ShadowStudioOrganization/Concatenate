package org.shadow.studio.concatenate.backend.data.launchermeta

data class CurseforgeMetaManifest(
    val minecraft: Minecraft,
    val manifestType: String,
    val manifestVersion: Int? = null,
    val name: String,
    val version: String,
    val author: String,
    val files: List<ManifestFile>
)

data class Minecraft(
    val version: String,
    val modLoaders: ModLoaders
)

data class ModLoaders(
    val id: String,
    val primary: Boolean
)

data class ManifestFile(
    val projectID: Int,
    val fileID: Int,
    val required: Boolean
)