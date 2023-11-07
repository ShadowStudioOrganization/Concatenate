package org.shadow.studio.concatenate.backend.data.profile

import com.fasterxml.jackson.annotation.JsonProperty


data class LibraryItem(
    val name: String,
    val downloads: Downloads? = null,
    val extract: Extract? = null,
    val natives: Natives? = null,
    val rules: List<Rule>? = null,
    val url: String? = null
)

data class Extract(
    val exclude: List<String>
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
    @JsonProperty("natives-windows")
    val nativesWindows: Artifact? = null,
    @JsonProperty("natives-osx")
    val nativesOSX: Artifact? = null,
    @JsonProperty("natives-macos")
    val nativesMacos: Artifact? = null,
    @JsonProperty("natives-linux")
    val nativesLinux: Artifact? = null
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