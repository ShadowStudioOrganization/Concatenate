package org.shadow.studio.concatenate.backend.data.profile


data class LibraryItem(
    val name: String,
    val downloads: Downloads?,
    val natives: Natives? = null,
    val rules: List<Rule>? = null,
    val url: String? = null
)

data class Downloads(
    val artifact: Artifact,
    val classifiers: Classifiers? = null
)

data class Rule(
    val action: String,
    val os: RuleOS?,
    val features: Map<String, Boolean>?
)

data class RuleOS(
    val name: String? = null,
    val arch: String? = null,
    val version: String? = null
)

data class Classifiers(
    val nativesWindows: Artifact?,
    val nativesMacos: Artifact?,
    val nativesLinux: Artifact?
)

data class Natives(
    val osx: String?,
    val windows: String?,
    val linux: String?
)

data class Artifact(
    val path: String,
    val sha1: String,
    val size: Long,
    val url: String
)