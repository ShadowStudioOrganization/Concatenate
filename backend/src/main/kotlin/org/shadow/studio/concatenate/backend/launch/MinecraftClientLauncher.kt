package org.shadow.studio.concatenate.backend.launch

import org.shadow.studio.concatenate.backend.adapter.JavaAdapter
import java.io.File

open class MinecraftClientLauncher(
    private val adapter: JavaAdapter,
    override val environments: Map<String, String>,
    override val workingDirectory: File,
    override val version: MinecraftVersion
) : MinecraftLauncher() {

    // Get the path of Java
    val gameVersion = version.getVersionString()
    override val program = adapter.getJavaPath(gameVersion)

    override fun launch(): Process {
        val proc: Process? = null
        // Joint parameter
        val lib = "$workingDirectory \\libraries\\ "
        val ver = "$workingDirectory \\versions\\ "
        val clientid = ""
        val auth_xuid = ""
        val quickPlayPath = ""
        val Xmn = "256"
        val Xmx = "4096"
        val username = ""
        val uuid = "00000XXXXXXXXXXXXXXXXXXXXXX114514"
        val accessToken = "00000XXXXXXXXXXXXXXXXXXXXXX43124"
        val gameDir = "$workingDirectory \\versions\\flandrebakapack-1.20.1"
        val height = 480
        val width = 854

        val cmd = """
            @echo off 
            title 启动 - $gameVersion
            echo 游戏正在启动,请稍候...
            set APPDATA=" $workingDirectory "
            cd /D $workingDirectory "
            " $program \java.exe" ^
            -XX:+UseG1GC -XX:-UseAdaptiveSizePolicy ^
            -XX:-OmitStackTraceInFastThrow ^
            -Dfml.ignoreInvalidMinecraftCertificates=True ^
            -Dfml.ignorePatchDiscrepancies=True ^
            -Dlog4j2.formatMsgNoLookups=true ^
            -XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump ^
            -Djava.library.path=" $ver $gameVersion\$gameVersion-natives" ^
            -Djna.tmpdir=" $ver $gameVersion\$gameVersion-natives" ^
            -Dorg.lwjgl.system.SharedLibraryExtractPath=" $ver $gameVersion\$gameVersion-natives" ^
            -Dio.netty.native.workdir=" $ver $gameVersion\$gameVersion-natives" ^
            -Dminecraft.launcher.brand=PCL ^
            -Dminecraft.launcher.version=308 ^
            -cp " $lib com\github\oshi\oshi-core\6.2.2\oshi-core-6.2.2.jar;
                  $lib com\google\code\gson\gson\2.10\gson-2.10.jar;
                  $lib com\google\guava\failureaccess\1.0.1\failureaccess-1.0.1.jar;
                  $lib com\google\guava\guava\31.1-jre\guava-31.1-jre.jar;
                  $lib com\ibm\icu\icu4j\71.1\icu4j-71.1.jar;
                  $lib com\mojang\authlib\4.0.43\authlib-4.0.43.jar;
                  $lib com\mojang\blocklist\1.0.10\blocklist-1.0.10.jar;
                  $lib com\mojang\brigadier\1.1.8\brigadier-1.1.8.jar;
                  $lib com\mojang\datafixerupper\6.0.8\datafixerupper-6.0.8.jar;
                  $lib com\mojang\logging\1.1.1\logging-1.1.1.jar;
                  $lib com\mojang\patchy\2.2.10\patchy-2.2.10.jar;
                  $lib com\mojang\text2speech\1.17.9\text2speech-1.17.9.jar;
                  $lib commons-codec\commons-codec\1.15\commons-codec-1.15.jar;
                  $lib commons-io\commons-io\2.11.0\commons-io-2.11.0.jar;
                  $lib commons-logging\commons-logging\1.2\commons-logging-1.2.jar;
                  $lib io\netty\netty-buffer\4.1.82.Final\netty-buffer-4.1.82.Final.jar;
                  $lib io\netty\netty-codec\4.1.82.Final\netty-codec-4.1.82.Final.jar;
                  $lib io\netty\netty-common\4.1.82.Final\netty-common-4.1.82.Final.jar;
                  $lib io\netty\netty-handler\4.1.82.Final\netty-handler-4.1.82.Final.jar;
                  $lib io\netty\netty-resolver\4.1.82.Final\netty-resolver-4.1.82.Final.jar;
                  $lib io\netty\netty-transport-classes-epoll\4.1.82.Final\netty-transport-classes-epoll-4.1.82.Final.jar;
                  $lib io\netty\netty-transport-native-unix-common\4.1.82.Final\netty-transport-native-unix-common-4.1.82.Final.jar;
                  $lib io\netty\netty-transport\4.1.82.Final\netty-transport-4.1.82.Final.jar;
                  $lib it\unimi\dsi\fastutil\8.5.9\fastutil-8.5.9.jar;
                  $lib net\java\dev\jna\jna-platform\5.12.1\jna-platform-5.12.1.jar;
                  $lib net\java\dev\jna\jna\5.12.1\jna-5.12.1.jar;
                  $lib net\sf\jopt-simple\jopt-simple\5.0.4\jopt-simple-5.0.4.jar;
                  $lib org\apache\commons\commons-compress\1.21\commons-compress-1.21.jar;
                  $lib org\apache\commons\commons-lang3\3.12.0\commons-lang3-3.12.0.jar;
                  $lib org\apache\httpcomponents\httpclient\4.5.13\httpclient-4.5.13.jar;
                  $lib org\apache\httpcomponents\httpcore\4.4.15\httpcore-4.4.15.jar;
                  $lib org\apache\logging\log4j\log4j-api\2.19.0\log4j-api-2.19.0.jar;
                  $lib org\apache\logging\log4j\log4j-core\2.19.0\log4j-core-2.19.0.jar;
                  $lib org\apache\logging\log4j\log4j-slf4j2-impl\2.19.0\log4j-slf4j2-impl-2.19.0.jar;
                  $lib org\joml\joml\1.10.5\joml-1.10.5.jar;
                  $lib org\lwjgl\lwjgl-glfw\3.3.1\lwjgl-glfw-3.3.1.jar;
                  $lib org\lwjgl\lwjgl-glfw\3.3.1\lwjgl-glfw-3.3.1-natives-windows.jar;
                  $lib org\lwjgl\lwjgl-glfw\3.3.1\lwjgl-glfw-3.3.1-natives-windows-arm64.jar;
                  $lib org\lwjgl\lwjgl-glfw\3.3.1\lwjgl-glfw-3.3.1-natives-windows-x86.jar;
                  $lib org\lwjgl\lwjgl-jemalloc\3.3.1\lwjgl-jemalloc-3.3.1.jar;
                  $lib org\lwjgl\lwjgl-jemalloc\3.3.1\lwjgl-jemalloc-3.3.1-natives-windows.jar;
                  $lib org\lwjgl\lwjgl-jemalloc\3.3.1\lwjgl-jemalloc-3.3.1-natives-windows-arm64.jar;
                  $lib org\lwjgl\lwjgl-jemalloc\3.3.1\lwjgl-jemalloc-3.3.1-natives-windows-x86.jar;
                  $lib org\lwjgl\lwjgl-openal\3.3.1\lwjgl-openal-3.3.1.jar;
                  $lib org\lwjgl\lwjgl-openal\3.3.1\lwjgl-openal-3.3.1-natives-windows.jar;
                  $lib org\lwjgl\lwjgl-openal\3.3.1\lwjgl-openal-3.3.1-natives-windows-arm64.jar;
                  $lib org\lwjgl\lwjgl-openal\3.3.1\lwjgl-openal-3.3.1-natives-windows-x86.jar;
                  $lib org\lwjgl\lwjgl-opengl\3.3.1\lwjgl-opengl-3.3.1.jar;
                  $lib org\lwjgl\lwjgl-opengl\3.3.1\lwjgl-opengl-3.3.1-natives-windows.jar;
                  $lib org\lwjgl\lwjgl-opengl\3.3.1\lwjgl-opengl-3.3.1-natives-windows-arm64.jar;
                  $lib org\lwjgl\lwjgl-opengl\3.3.1\lwjgl-opengl-3.3.1-natives-windows-x86.jar;
                  $lib org\lwjgl\lwjgl-stb\3.3.1\lwjgl-stb-3.3.1.jar;
                  $lib org\lwjgl\lwjgl-stb\3.3.1\lwjgl-stb-3.3.1-natives-windows.jar;
                  $lib org\lwjgl\lwjgl-stb\3.3.1\lwjgl-stb-3.3.1-natives-windows-arm64.jar;
                  $lib org\lwjgl\lwjgl-stb\3.3.1\lwjgl-stb-3.3.1-natives-windows-x86.jar;
                  $lib org\lwjgl\lwjgl-tinyfd\3.3.1\lwjgl-tinyfd-3.3.1.jar;
                  $lib org\lwjgl\lwjgl-tinyfd\3.3.1\lwjgl-tinyfd-3.3.1-natives-windows.jar;
                  $lib org\lwjgl\lwjgl-tinyfd\3.3.1\lwjgl-tinyfd-3.3.1-natives-windows-arm64.jar;
                  $lib org\lwjgl\lwjgl-tinyfd\3.3.1\lwjgl-tinyfd-3.3.1-natives-windows-x86.jar;
                  $lib org\lwjgl\lwjgl\3.3.1\lwjgl-3.3.1.jar;
                  $lib org\lwjgl\lwjgl\3.3.1\lwjgl-3.3.1-natives-windows.jar;
                  $lib org\lwjgl\lwjgl\3.3.1\lwjgl-3.3.1-natives-windows-arm64.jar;
                  $lib org\lwjgl\lwjgl\3.3.1\lwjgl-3.3.1-natives-windows-x86.jar;
                  $lib org\slf4j\slf4j-api\2.0.1\slf4j-api-2.0.1.jar;
                  $lib net\fabricmc\tiny-mappings-parser\0.3.0+build.17\tiny-mappings-parser-0.3.0+build.17.jar;
                  $lib net\fabricmc\sponge-mixin\0.12.5+mixin.0.8.5\sponge-mixin-0.12.5+mixin.0.8.5.jar;
                  $lib net\fabricmc\tiny-remapper\0.8.2\tiny-remapper-0.8.2.jar;
                  $lib net\fabricmc\access-widener\2.1.0\access-widener-2.1.0.jar;
                  $lib org\ow2\asm\asm\9.6\asm-9.6.jar;
                  $lib org\ow2\asm\asm-analysis\9.6\asm-analysis-9.6.jar;
                  $lib org\ow2\asm\asm-commons\9.6\asm-commons-9.6.jar;
                  $lib org\ow2\asm\asm-tree\9.6\asm-tree-9.6.jar;
                  $lib org\ow2\asm\asm-util\9.6\asm-util-9.6.jar;
                  $lib net\fabricmc\intermediary\1.20.1\intermediary-1.20.1.jar;
                  $lib net\fabricmc\fabric-loader\0.14.24\fabric-loader-0.14.24.jar;
                  $workingDirectory \versions\$gameVersion\$gameVersion.jar" ^
            -DFabricMcEmu=net.minecraft.client.main.Main ^
            -Xmn $Xmn m ^
            -Xmx $Xmx m ^
            -Dlog4j2.formatMsgNoLookups=true ^
            --add-exports cpw.mods.bootstraplauncher/cpw.mods.bootstraplauncher=ALL-UNNAMED ^
            -Doolloo.jlw.tmpdir="C:\Users\whiter\AppData\Roaming\PCL" ^
            -jar "C:\Users\whiter\AppData\Roaming\PCL\JavaWrapper.jar" net.fabricmc.loader.impl.launch.knot.KnotClient ^
            --username $username ^
            --version $gameVersion ^
            --gameDir " $gameDir " ^
            --assetsDir " $workingDirectory \assets" ^
            --assetIndex 5 ^
            --uuid $uuid ^
            --accessToken $accessToken ^
            --clientId $clientid
            --xuid $auth_xuid ^
            --userType msa ^zh
            --versionType PCL ^
            --width $width ^
            --height $height ^
            --quickPlayPath $quickPlayPath
            echo 游戏已退出.
            pause
        """

        return proc!!
    }

}