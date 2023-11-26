package org.shadow.studio.concatenate.backend.data.profile

import com.fasterxml.jackson.annotation.JsonProperty

data class JsonProfile(
    val arguments: Arguments?,
    val assetIndex: AssetIndex,
    val assets: String,
    val clientVersion: String?,
    val complianceLevel: Int?,
    val downloads: GameDownloads,
    val id: String,
    val javaVersion: JavaVersion?, // b1.9-pre6 missing
    val logging: Logging?,
    val libraries: List<LibraryItem>,
    val mainClass: String,
    val minimumLauncherVersion: Int,
    val releaseTime: String,
    val time: String,
    val type: String,
    val minecraftArguments: String?
)

data class Arguments(
    val game: List<Any>,
    val jvm: List<Any>
)

data class ComplexMinecraftArgument(
    val rules: List<Rule>?,
    val value: Any
)

data class AssetIndex(
    val id: String,
    val sha1: String,
    val size: Long,
    val totalSize: Long,
    val url: String
)

data class GameDownloads(
    val client: GameDownloadItem,
    @JsonProperty("client_mappings")
    val clientMappings: GameDownloadItem? = null,
    val server: GameDownloadItem? = null,
    @JsonProperty("server_mappings")
    val serverMappings: GameDownloadItem? = null,
    @JsonProperty("windows_server")
    val windowsServer: GameDownloadItem? = null
)

data class JavaVersion(
    val component: String,
    val majorVersion: Int
)

data class Logging(
    val client: ClientLogging
)

data class GameDownloadItem(
    val url: String,
    val size: Long,
    val sha1: String? // b1.9-pre6 missing
)

data class ClientLogging(
    val argument: String,
    val `file`: File,
    val type: String
)

data class File(
    val id: String,
    val sha1: String,
    val size: Long,
    val url: String
)

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
    val artifact: Artifact?,
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
    val nativesLinux: Artifact? = null,

    @JsonProperty("natives-windows-32")
    val nativesWindows32: Artifact?,

    @JsonProperty("natives-windows-64")
    val nativesWindows64: Artifact?,

    val javadoc: Artifact? = null,
    val sources: Artifact? = null,
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