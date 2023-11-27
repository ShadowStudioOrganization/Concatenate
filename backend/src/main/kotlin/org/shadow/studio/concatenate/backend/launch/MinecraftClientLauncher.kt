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
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

/**
 * @param isCheckFileIntegrity 启动前是否检查文件完整性
 */
class MinecraftClientLauncher(
    private val adapter: JavaAdapter,
    private val clientCfg: MinecraftClientConfiguration,
    override val workingDirectory: File,
    override val version: MinecraftVersion,
    override val environments: Map<String, String> = mapOf(),
    private val loginMethod: LoginMethod = OfflineMethod("Steve"),
    private val logger: Logger = LoggerFactory.getLogger(MinecraftClientLauncher::class.java),
    private val isCheckFileIntegrity: Boolean = true,
    private val resolver: MinecraftResourceResolver
        = MinecraftResourceResolver(NormalDirectoryLayer(workingDirectory, version.isolated, version.versionName), version),
    private val checker: MinecraftResourceChecker = MinecraftResourceChecker(logger),
    private val isConfiguratorMinecraftLogging: Boolean = false
) : MinecraftLauncher() {

    override suspend fun launch(): MinecraftClientInstance = withContext(Dispatchers.IO) {
        val profile = version.profile
        val versionJar = resolver.resolveGameJar()

        if (isCheckFileIntegrity) {
            val isJarDamaged = !checker.checkVersionJar(profile, versionJar)
            val assetsDamagedFiles = checker.checkAssetsObjects(resolver.resolveAssetIndexJsonFile(), resolver.resolveAssetObjectsRoot())
            val classpathDamagedFiles = checker.checkClasspath(profile.libraries, resolver.resolveLibrariesRoot())

            if (isJarDamaged) {
                error("could not launch minecraft, since $versionJar is damaged or missing.")
            }

            if (classpathDamagedFiles.isNotEmpty()) {
                val list = classpathDamagedFiles.map {
                    "\n        >>located at " + it.absolutePath + if (!it.exists()) " [missing]" else " [damaged]"
                }
                error("could not launch minecraft, since the following libraries are damaged or missing: ${list.joinToString("")}")
            }

            if (assetsDamagedFiles.isNotEmpty()) {
                val list = assetsDamagedFiles.map {
                    "\n        >>located at " + it.absolutePath + if (!it.exists()) " [missing]" else " [damaged]"
                }
                error("could not launch minecraft, since the following assets are damaged or missing: ${list.joinToString("")}")
            }
        }

        val javaBinary = adapter.getJavaBinary(version, clientCfg.preferJavaVersion)?.path?.toFile() ?: error("no suitable java binary found.")

        val nativesDirectory = resolver.resolveNatives()

        val loginInfo = loginMethod.login()

        val command = buildList<String> {
            add(javaBinary.absolutePath.wrapDoubleQuote())

            add(resolver.resolveComplexJvmArguments(buildMap {
                put("version_name", version.versionName.wrapDoubleQuote())
                put("classpath", resolver
                    .resolveClasspath(true)
                    .joinToString(File.pathSeparator) { it.absolutePath }
                    .wrapDoubleQuote()
                )
                put("launcher_name", Concatenate.launcherName)
                put("natives_directory", nativesDirectory.absolutePath.wrapDoubleQuote())
                put("library_directory", resolver.resolveLibrariesRoot().absolutePath)
                put("launcher_version", Concatenate.launcherVersion)
                put("classpath_separator", File.pathSeparator)
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

                    // low version
                    put("user_properties", loginInfo.userProperties)
                    put("auth_session", loginInfo.authSession)
                    put("game_assets", resolver.resolveAssetRoot().absolutePath.wrapDoubleQuote())

                    put("version_name", versionJar.nameWithoutExtension)
                    put("assets_root", resolver.resolveAssetRoot().absolutePath.wrapDoubleQuote())
                    put("assets_index_name", profile.assetIndex.id)
                    put("clientid", clientCfg.clientId)
                    put("user_type", clientCfg.userType)
                    put("version_type", clientCfg.versionType)
                    put("game_directory", resolver.resolveGameDirectory().absolutePath.wrapDoubleQuote())

                    putAll(clientCfg.featureGameArguments)
                },
                clientCfg.clientRuleFeatures
            ))
            add(clientCfg.customUserArguments)
        }

        debugCommands(command)

        val processBuilder = ProcessBuilder().apply {
            command(command)
            environment().putAll(environments)
            directory(workingDirectory)
        }

        MinecraftClientInstance(
            process = processBuilder.start(),
            processBuilder = processBuilder
        )
    }

    protected fun debugCommands(command: List<String>) {
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
}