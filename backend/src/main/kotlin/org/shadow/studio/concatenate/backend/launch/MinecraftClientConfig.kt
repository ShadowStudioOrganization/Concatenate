package org.shadow.studio.concatenate.backend.launch

import org.shadow.studio.concatenate.backend.util.ListBuilder

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
    val customJvmArguments = mutableListOf<String>()
    val customUserArguments = mutableListOf<String>()

    fun customJvmArguments(block: ListBuilder<String>.() -> Unit) {
        block(ListBuilder(customJvmArguments))
    }

    fun customUserArguments(block: ListBuilder<String>.() -> Unit) {
        block(ListBuilder(customUserArguments))
    }

    fun fileEncoding(encoding: String) {
        extraJvmArguments["file_encoding"] = encoding
    }

    fun sunStdoutEncoding(encoding: String) {
        extraJvmArguments["sun_stdout_encoding"] = encoding
    }

    fun sunStderrEncoding(encoding: String) {
        extraJvmArguments["sun_stderr_encoding"] = encoding
    }

    fun initialJavaHeapSize(size: String) {
        extraJvmArguments["initial_java_heap_size"] = size
    }

    fun maximumJavaHeapSize(size: String) {
        extraJvmArguments["maximum_Java_heap_size"] = size
    }

    fun youngGenerationSize(size: String) {
        extraJvmArguments["the_young_generation_size"] = size
    }

    fun useG1GC(enable: Boolean) {
        extraJvmArguments["use_g1gc"] = enable.toString()
    }

    fun useAdaptiveSizePolicy(enable: Boolean) {
        extraJvmArguments["use_adaptive_size_policy"] = enable.toString()
    }

    fun omitStacktraceInFastThrow(enable: Boolean) {
        extraJvmArguments["omit_stacktrace_in_fast_throw"] = enable.toString()
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
