package org.shadow.studio.concatenate.backend.data.launchscript

data class LaunchJson(
    val arguments: Arguments,
    val assetIndex: AssetIndex,
    val assets: String,
    val clientVersion: String,
    val complianceLevel: Int,
    val downloads: Downloads,
    val id: String,
    val javaVersion: JavaVersion,
    val libraries: List<Library>,
    val logging: Logging,
    val mainClass: String,
    val minimumLauncherVersion: Int,
    val releaseTime: String,
    val time: String,
    val type: String
)