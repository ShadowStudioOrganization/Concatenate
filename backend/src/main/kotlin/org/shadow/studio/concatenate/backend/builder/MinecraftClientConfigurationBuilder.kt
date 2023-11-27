package org.shadow.studio.concatenate.backend.builder

import org.shadow.studio.concatenate.backend.data.launch.MinecraftExtraJvmArguments
import org.shadow.studio.concatenate.backend.util.LazyValueDelegate
import org.shadow.studio.concatenate.backend.launch.MinecraftClientConfiguration

class MinecraftClientConfigurationBuilder {

    var clientId: String = "\${clientid}"
    var versionType: String = "Concatenate"
    var userType: String = "msa"
    var initialJavaHeapSize: String? = null
    var maximumJavaHeapSize: String? = null
    var theYoungGenerationSize: String? = null
    var useG1GC: Boolean = false
    var useAdaptiveSizePolicy: Boolean = false
    var omitStacktraceInFastThrow: Boolean = false
    var fileEncoding: String? = null
    var stdoutEncoding: String? = null
    var stderrEncoding: String? = null
    var preferJavaVersion: Int? = null
    var minecraftExtraJvmArguments: MinecraftExtraJvmArguments by LazyValueDelegate {
        MinecraftExtraJvmArguments(
            initialJavaHeapSize,
            maximumJavaHeapSize,
            theYoungGenerationSize,
            useG1GC,
            useAdaptiveSizePolicy,
            omitStacktraceInFastThrow,
            fileEncoding,
            stdoutEncoding,
            stderrEncoding
        )
    }

    private val _clientRuleFeatures = mutableMapOf<String, Boolean>()
    private val _featureGameArguments = mutableMapOf<String, String>()
    private val _customJvmArguments = mutableListOf<String>()
    private val _customUserArguments = mutableListOf<String>()
    private var _onJvmArgumentsVariablePool: (MutableMap<String, String>.() -> Unit)? = null
    private var _onUserArgumentsVariablePool: (MutableMap<String, String>.() -> Unit)? = null

    fun customJvmArguments(block: MutableList<String>.() -> Unit) = _customJvmArguments.addAll(buildList(block))
    fun customUserArguments(block: MutableList<String>.() -> Unit) = _customUserArguments.addAll(buildList(block))

    fun modifyJvmArgumentVariablePool(block: MutableMap<String, String>.() -> Unit) {
        _onJvmArgumentsVariablePool = block
    }

    fun modifyUserArgumentVariablePool(block: MutableMap<String, String>.() -> Unit) {
        _onUserArgumentsVariablePool = block
    }

    fun enableFeature(name: String) {
        _clientRuleFeatures[name] = true
    }

    fun demoUser() {
        enableFeature("is_demo_user")
    }

    fun disableFeature(name: String) {
        _clientRuleFeatures[name] = false
    }

    fun quickPlayPath(path: String) {
        enableFeature("has_quick_plays_support")
        _featureGameArguments["quickPlayPath"] = path
    }

    fun quickPlayMultiplayer(arg: String) {
        enableFeature("is_quick_play_multiplayer")
        _featureGameArguments["quickPlayMultiplayer"] = arg
    }

    fun quickPlaySinglePlayer(arg: String) {
        enableFeature("is_quick_play_singleplayer")
        _featureGameArguments["quickPlaySingleplayer"] = arg
    }

    fun quickPlayRealms(arg: String) {
        enableFeature("is_quick_play_realms")
        _featureGameArguments["quickPlayRealms"] = arg
    }

    fun windowSize(width: Int, height: Int) {
        enableFeature("has_custom_resolution")
        _featureGameArguments["resolution_width"] = width.toString()
        _featureGameArguments["resolution_height"] = height.toString()
    }


    fun build() = MinecraftClientConfiguration(clientId, versionType, userType, minecraftExtraJvmArguments).apply {
        preferJavaVersion = this@MinecraftClientConfigurationBuilder.preferJavaVersion
        clientRuleFeatures += _clientRuleFeatures
        featureGameArguments += _featureGameArguments
        customJvmArguments += _customJvmArguments
        customUserArguments += _customUserArguments

        _onJvmArgumentsVariablePool?.let { onJvmArgumentsVariablePool = it }
        _onUserArgumentsVariablePool?.let { onUserArgumentsVariablePool = it }
    }

}