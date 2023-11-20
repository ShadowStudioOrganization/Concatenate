package org.shadow.studio.concatenate.backend.launch

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.shadow.studio.concatenate.backend.Concatenate
import org.shadow.studio.concatenate.backend.adapter.JavaAdapter
import org.shadow.studio.concatenate.backend.checksum.MinecraftResourceChecker
import org.shadow.studio.concatenate.backend.data.launch.MinecraftClientInstance
import org.shadow.studio.concatenate.backend.resolver.MinecraftResourceResolver
import org.shadow.studio.concatenate.backend.resolver.NormalDirectoryLayer
import org.shadow.studio.concatenate.backend.login.LoginMethod
import org.shadow.studio.concatenate.backend.login.OfflineMethod
import org.shadow.studio.concatenate.backend.util.*
import org.slf4j.LoggerFactory
import java.io.File

class MinecraftClientLauncher(
    adapter: JavaAdapter,
    private val clientCfg: MinecraftClientConfiguration,
    override val workingDirectory: File,
    override val version: MinecraftVersion,
    override val environments: Map<String, String> = mapOf(),
    private val loginMethod: LoginMethod = OfflineMethod("Steve"),
    private val isCheckFileIntegrity: Boolean = true,
    private val resolver: MinecraftResourceResolver = MinecraftResourceResolver(NormalDirectoryLayer(workingDirectory, version)),
    private val checker: MinecraftResourceChecker = MinecraftResourceChecker()
) : MinecraftLauncher() {

    // Get the path of Java
    override val program = adapter.getJavaBin(version.versionId)
    private val logger = LoggerFactory.getLogger(MinecraftClientLauncher::class.java)
    private var isConfiguratorMinecraftLogging = true

    override suspend fun launch(): MinecraftClientInstance = withContext(Dispatchers.IO) {
        val profile = version.profile
        val versionJar = resolver.resolveGameJar()
        if (isCheckFileIntegrity) {
            checker.checkVersionJar(profile, versionJar)
            checker.checkAssetsObjects(resolver.resolveAssetIndexJsonFile(), resolver.resolveAssetObjectsRoot())
            checker.checkClasspath(profile.libraries, resolver.resolveLibrariesRoot())
        }
        val gameDirectory = resolver.resolveGameDirectory()
        val javaBin = File(program)
        checkExists(javaBin)

        val nativesDirectory = resolver.resolveNatives()

        val loginInfo = loginMethod.login()

        val command = buildList<String> {
            add(javaBin.absolutePath.wrapDoubleQuote())
            add(resolver.resolveComplexJvmArguments(buildMap {
                put("classpath", resolver
                    .resolveClasspath(true)
                    .joinToString(File.pathSeparator) { it.absolutePath }
                    .wrapDoubleQuote()
                )
                put("launcher_name", Concatenate.launcherName)
                put("natives_directory", nativesDirectory.absolutePath.wrapDoubleQuote())
                put("launcher_version", Concatenate.launcherVersion)
            }))
            add(resolver.resolveExtraJvmArguments(clientCfg.minecraftExtraJvmArguments))
            if (isConfiguratorMinecraftLogging) {
                add(resolver.resolveLoggingArguments(mapOf(
                    "path" to resolver.resolveLoggingConfigurationFile().absolutePath.wrapDoubleQuote()
                )))
            }
            add("-Dminecraft.client.jar=" + versionJar.absolutePath.wrapDoubleQuote())
            add(clientCfg.customJvmArguments)
            add(loginMethod.insertJvmArguments())
            add(profile.mainClass)
            add(resolver.resolveComplexMinecraftArguments(
                buildMap<String, String> {
                    put("auth_player_name", loginInfo.authPlayerName)
                    put("auth_uuid", loginInfo.authUUID)
                    put("auth_xuid", loginInfo.authXUID)
                    put("auth_access_token", loginInfo.authAccessToken)
                    put("version_name", versionJar.nameWithoutExtension)
                    put("assets_root", resolver.resolveAssetRoot().absolutePath.wrapDoubleQuote())
                    put("assets_index_name", profile.assetIndex.id)
                    put("clientid", clientCfg.clientId)
                    put("user_type", clientCfg.userType)
                    put("version_type", clientCfg.versionType)
                    putAll(clientCfg.featureGameArguments)
                },
                clientCfg.clientRuleFeatures
            ))
            add(clientCfg.customUserArguments)
        }

        debugCommands(command)

        val processBuilder = ProcessBuilder(command).directory(workingDirectory)

        MinecraftClientInstance(
            process = processBuilder.start(),
            processBuilder = processBuilder
        )
    }

    private fun debugCommands(command: List<String>) {
        logger.debug("final arguments")
        var isReachingAccessToken = false
        command.forEach {
            if (isReachingAccessToken) {
                logger.debug("**********")
                isReachingAccessToken = false
            } else logger.debug(it)

            if (it.trim() == "--accessToken") isReachingAccessToken = true
        }
    }

    private fun checkExists(file: File) {
         if (!file.exists()) error("file/dir ${file.absolutePath} is not exists")
    }

    fun disableLoggingConfiguration() {
        isConfiguratorMinecraftLogging = false
    }

}