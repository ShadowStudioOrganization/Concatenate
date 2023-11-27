package org.shadow.studio.concatenate.backend.launch

import org.shadow.studio.concatenate.backend.data.launch.MinecraftExtraJvmArguments
import org.shadow.studio.concatenate.backend.util.ListBuilder

class MinecraftClientConfiguration(
    val clientId: String = "\${clientid}",
    val versionType: String = "Concatenate",
    val userType: String = "msa",
    val minecraftExtraJvmArguments: MinecraftExtraJvmArguments = MinecraftExtraJvmArguments()
) {
    var preferJavaVersion: Int? = 8
    internal val clientRuleFeatures = mutableMapOf<String, Boolean>()
    internal val featureGameArguments = mutableMapOf<String, String>()
    internal val customJvmArguments = mutableListOf<String>()
    internal val customUserArguments = mutableListOf<String>()

    fun customJvmArguments(block: ListBuilder<String>.() -> Unit) {
        block(ListBuilder(customJvmArguments))
    }

    fun customUserArguments(block: ListBuilder<String>.() -> Unit) {
        block(ListBuilder(customUserArguments))
    }

    fun enableFeature(name: String) {
        clientRuleFeatures[name] = true
    }

    fun demoUser() {
        enableFeature("is_demo_user")
    }

    fun disableFeature(name: String) {
        clientRuleFeatures[name] = false
    }

    fun quickPlayPath(path: String) {
        enableFeature("has_quick_plays_support")
        featureGameArguments["quickPlayPath"] = path
    }

    fun quickPlayMultiplayer(arg: String) {
        enableFeature("is_quick_play_multiplayer")
        featureGameArguments["quickPlayMultiplayer"] = arg
    }

    fun quickPlaySinglePlayer(arg: String) {
        enableFeature("is_quick_play_singleplayer")
        featureGameArguments["quickPlaySingleplayer"] = arg
    }

    fun quickPlayRealms(arg: String) {
        enableFeature("is_quick_play_realms")
        featureGameArguments["quickPlayRealms"] = arg
    }

    fun windowSize(width: Int, height: Int) {
        enableFeature("has_custom_resolution")
        featureGameArguments["resolution_width"] = width.toString()
        featureGameArguments["resolution_height"] = height.toString()
    }
}
