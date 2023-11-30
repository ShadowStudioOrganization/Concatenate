package org.shadow.studio.concatenate.backend.builder

import ch.qos.logback.classic.Level
import org.shadow.studio.concatenate.backend.adapter.JavaAdapter
import org.shadow.studio.concatenate.backend.adapter.JavaFinder
import org.shadow.studio.concatenate.backend.data.jrelocalpath.JavaRuntimeLocation
import org.shadow.studio.concatenate.backend.checksum.MinecraftResourceChecker
import org.shadow.studio.concatenate.backend.util.LazyValueDelegate
import org.shadow.studio.concatenate.backend.launch.MinecraftClientConfiguration
import org.shadow.studio.concatenate.backend.launch.MinecraftClientLauncher
import org.shadow.studio.concatenate.backend.launch.MinecraftVersion
import org.shadow.studio.concatenate.backend.login.LoginMethod
import org.shadow.studio.concatenate.backend.login.OfflineMethod
import org.shadow.studio.concatenate.backend.resolver.DirectoryLayer
import org.shadow.studio.concatenate.backend.resolver.MinecraftResourceResolver
import org.shadow.studio.concatenate.backend.resolver.NormalDirectoryLayer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Path

class MinecraftClientLauncherBuilder {
    private var isConfigFlag: Boolean = false
    private var _workingDirectory: File? = null
    private var _versionName: String? = null

    var versionIsolate: Boolean = false
    var environments: Map<String, String> by LazyValueDelegate { mapOf() }
    var loginMethod: LoginMethod by LazyValueDelegate { OfflineMethod("Steve") }
    var logger: Logger by LazyValueDelegate { LoggerFactory.getLogger(MinecraftClientLauncher::class.java) }
    var checkFileIntegrity: Boolean = true
    val checker: MinecraftResourceChecker by LazyValueDelegate { MinecraftResourceChecker(logger) }
    val configuratorMinecraftLogging: Boolean = false
    var javaFinder: JavaFinder by LazyValueDelegate { JavaFinder() }
    var javaAdapter: JavaAdapter by LazyValueDelegate { JavaAdapter(javaFinder) }
    var directoryLayer: DirectoryLayer by LazyValueDelegate { NormalDirectoryLayer(workingDirectory, versionIsolate, versionName) }
    var version: MinecraftVersion by LazyValueDelegate { directoryLayer.newMinecraftVersion() }
    var resolver: MinecraftResourceResolver by LazyValueDelegate { MinecraftResourceResolver(directoryLayer, version) }
    var clientConfiguration: MinecraftClientConfiguration by LazyValueDelegate { MinecraftClientConfigurationBuilder().build() }

    var workingDirectory: File
        get() = _workingDirectory ?: error("working directory name has not been set")
        set(value) { _workingDirectory = value }

    var versionName: String
        get() = _versionName ?: error("version name has not been set")
        set(value) { _versionName = value }

    fun setLaunchLoggerLevel(level: Level) {
        (logger as ch.qos.logback.classic.Logger).level = level
    }

    fun login(block: () -> Unit): Unit = TODO("login impl")

    fun clientConfig(block: MinecraftClientConfigurationBuilder.() -> Unit) {
        if (!this.isConfigFlag) {
            val builder = MinecraftClientConfigurationBuilder()
            builder.block()
            clientConfiguration = builder.build()
            this.isConfigFlag = true
        } else error("client configuration can only call once.")
    }

    fun adapterMinecraftWithJava(selector: List<JavaRuntimeLocation>.(MinecraftVersion) -> JavaRuntimeLocation) {
        object : JavaAdapter() {
            override suspend fun getJavaBinary(version: MinecraftVersion, preferVersion: Int?): JavaRuntimeLocation {
                return candidate.selector(version)
            }
        }
    }

    fun javaBinaryLocations(block: MutableMap<String, Path>.() -> Unit) {
        javaFinder = object : JavaFinder() {
            override suspend fun find(): List<JavaRuntimeLocation> {
                return buildList<JavaRuntimeLocation> {
                    this += super.find()
                    this += buildMap(block).map { (versionString, path) -> JavaRuntimeLocation(path, true, versionString) }
                }
            }
        }
    }

    fun setWorkingDirectory(wd: File) = apply { this._workingDirectory = wd }
    fun setWorkingDirectory(wd: Path) = apply { this._workingDirectory = wd.toFile() }
    fun setWorkingDirectory(wd: String) = apply { this._workingDirectory = File(wd) }

    fun build(): MinecraftClientLauncher {
        return MinecraftClientLauncher(
            javaAdapter,
            clientConfiguration,
            workingDirectory,
            version,
            environments,
            loginMethod,
            logger,
            checkFileIntegrity,
            resolver,
            checker,
            configuratorMinecraftLogging
        )
    }
}

