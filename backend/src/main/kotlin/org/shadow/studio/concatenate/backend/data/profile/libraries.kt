package org.shadow.studio.concatenate.backend.data.profile

interface LibraryItem {
    val name: String
}

class CommonLibraryItem(
    override val name: String,
    val downloads: Downloads,
    val natives: Natives? = null,
    val rules: List<Rule>? = null
) : LibraryItem

class FabricLibraryItem(override val name: String, val url: String) : LibraryItem

class Downloads(
    val artifact: Artifact,
    val classifiers: Classifiers? = null
)

class Rule(
    val action: String,
    val os: RuleOS?,
    val features: Map<String, Boolean>?
)

class RuleOS(
    val name: String
)

class Classifiers(
    val nativesWindows: Artifact?,
    val nativesMacos: Artifact?,
    val nativesLinux: Artifact?
)

class Natives(
    val osx: String?,
    val windows: String?,
    val linux: String?
)

class Artifact(
    val path: String,
    val sha1: String,
    val size: Long,
    val url: String
)