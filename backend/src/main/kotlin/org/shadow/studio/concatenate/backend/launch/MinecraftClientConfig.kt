package org.shadow.studio.concatenate.backend.launch

class MinecraftClientConfig(
    val authPlayerName: String,
    val authUUID: String,
    val authAccessToken: String,
    val authXXUID: String = "\${auth_xuid}",
    val clientId: String = "\${clientid}",
    val versionType: String = "Concatenate",
    val userType: String = "msa"
) {

    val clientRuleFeatures = mutableMapOf<String, Boolean>()
    val featureGameArguments = mutableMapOf<String, String>()
    val extraJvmArguments = mutableMapOf<String, String>()

    fun initialJavaHeapSize(size: String) {
        extraJvmArguments["initial_java_heap_size"] = size
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
