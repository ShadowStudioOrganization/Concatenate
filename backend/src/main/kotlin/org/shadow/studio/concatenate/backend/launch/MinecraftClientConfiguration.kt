package org.shadow.studio.concatenate.backend.launch

import org.shadow.studio.concatenate.backend.data.launch.MinecraftExtraJvmArguments

class MinecraftClientConfiguration(
    internal val clientId: String = "\${clientid}",
    internal val versionType: String = "Concatenate",
    internal val userType: String = "msa",
    internal val minecraftExtraJvmArguments: MinecraftExtraJvmArguments = MinecraftExtraJvmArguments()
) {
    internal var preferJavaVersion: Int? = null
    internal val clientRuleFeatures = mutableMapOf<String, Boolean>()
    internal val featureGameArguments = mutableMapOf<String, String>()
    internal val customJvmArguments = mutableListOf<String>()
    internal val customUserArguments = mutableListOf<String>()
    internal var onJvmArgumentsVariablePool: (MutableMap<String, String>.() -> Unit)? = null
    internal var onUserArgumentsVariablePool: (MutableMap<String, String>.() -> Unit)? = null

    fun modifyUserArgumentsVariablePool(pool: MutableMap<String, String>) {
        onUserArgumentsVariablePool?.invoke(pool)
    }

    fun modifyJvmArgumentsVariablePool(pool: MutableMap<String, String>) {
        onJvmArgumentsVariablePool?.invoke(pool)
    }

}
