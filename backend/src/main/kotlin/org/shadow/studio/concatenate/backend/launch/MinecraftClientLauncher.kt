package org.shadow.studio.concatenate.backend.launch

import org.shadow.studio.concatenate.backend.adapter.JavaAdapter
import java.io.File

open class MinecraftClientLauncher(
    private val adapter: JavaAdapter,
    override val environments: Map<String, String>,
    override val workingDirectory: File,
    override val version: MinecraftVersion
) : MinecraftLauncher() {
// 把你的屎山优化一下再push，好
    override val program = adapter.getJavaPath(version)

    override fun launch(): Process {
        val proc: Process? = null
        // Joint parameter
        val title: String = "title 启动 - flandrebakapack-1.20.1"
        val echo: String = "echo 游戏正在启动，请稍候。"
        val set: String = "set APPDATA=\""+ workingDirectory +"\""
        val cd: String = "cd /D\""+ workingDirectory +"\""
        val clientid = ""
        val auth_xuid = ""
        val quickPlayPath = ""

        val cmd = """
        @echo off $title $echo $set $cd

"D:\Environments\java\17\bin\java.exe" ^
-XX:+UseG1GC -XX:-UseAdaptiveSizePolicy ^
-XX:-OmitStackTraceInFastThrow ^
-Dfml.ignoreInvalidMinecraftCertificates=True ^
-Dfml.ignorePatchDiscrepancies=True ^
-Dlog4j2.formatMsgNoLookups=true ^
-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump ^
-Djava.library.path="D:\Games\aloneg\versions\flandrebakapack-1.20.1\flandrebakapack-1.20.1-natives" ^
-Djna.tmpdir="D:\Games\aloneg\versions\flandrebakapack-1.20.1\flandrebakapack-1.20.1-natives" ^
-Dorg.lwjgl.system.SharedLibraryExtractPath="D:\Games\aloneg\versions\flandrebakapack-1.20.1\flandrebakapack-1.20.1-natives" ^
-Dio.netty.native.workdir="D:\Games\aloneg\versions\flandrebakapack-1.20.1\flandrebakapack-1.20.1-natives" ^
-Dminecraft.launcher.brand=PCL ^
-Dminecraft.launcher.version=308 ^
-cp "D:\Games\aloneg\libraries\com\github\oshi\oshi-core\6.2.2\oshi-core-6.2.2.jar;D:\Games\aloneg\libraries\com\google\code\gson\gson\2.10\gson-2.10.jar;D:\Games\aloneg\libraries\com\google\guava\failureaccess\1.0.1\failureaccess-1.0.1.jar;D:\Games\aloneg\libraries\com\google\guava\guava\31.1-jre\guava-31.1-jre.jar;D:\Games\aloneg\libraries\com\ibm\icu\icu4j\71.1\icu4j-71.1.jar;D:\Games\aloneg\libraries\com\mojang\authlib\4.0.43\authlib-4.0.43.jar;D:\Games\aloneg\libraries\com\mojang\blocklist\1.0.10\blocklist-1.0.10.jar;D:\Games\aloneg\libraries\com\mojang\brigadier\1.1.8\brigadier-1.1.8.jar;D:\Games\aloneg\libraries\com\mojang\datafixerupper\6.0.8\datafixerupper-6.0.8.jar;D:\Games\aloneg\libraries\com\mojang\logging\1.1.1\logging-1.1.1.jar;D:\Games\aloneg\libraries\com\mojang\patchy\2.2.10\patchy-2.2.10.jar;D:\Games\aloneg\libraries\com\mojang\text2speech\1.17.9\text2speech-1.17.9.jar;D:\Games\aloneg\libraries\commons-codec\commons-codec\1.15\commons-codec-1.15.jar;D:\Games\aloneg\libraries\commons-io\commons-io\2.11.0\commons-io-2.11.0.jar;D:\Games\aloneg\libraries\commons-logging\commons-logging\1.2\commons-logging-1.2.jar;D:\Games\aloneg\libraries\io\netty\netty-buffer\4.1.82.Final\netty-buffer-4.1.82.Final.jar;D:\Games\aloneg\libraries\io\netty\netty-codec\4.1.82.Final\netty-codec-4.1.82.Final.jar;D:\Games\aloneg\libraries\io\netty\netty-common\4.1.82.Final\netty-common-4.1.82.Final.jar;D:\Games\aloneg\libraries\io\netty\netty-handler\4.1.82.Final\netty-handler-4.1.82.Final.jar;D:\Games\aloneg\libraries\io\netty\netty-resolver\4.1.82.Final\netty-resolver-4.1.82.Final.jar;D:\Games\aloneg\libraries\io\netty\netty-transport-classes-epoll\4.1.82.Final\netty-transport-classes-epoll-4.1.82.Final.jar;D:\Games\aloneg\libraries\io\netty\netty-transport-native-unix-common\4.1.82.Final\netty-transport-native-unix-common-4.1.82.Final.jar;D:\Games\aloneg\libraries\io\netty\netty-transport\4.1.82.Final\netty-transport-4.1.82.Final.jar;D:\Games\aloneg\libraries\it\unimi\dsi\fastutil\8.5.9\fastutil-8.5.9.jar;D:\Games\aloneg\libraries\net\java\dev\jna\jna-platform\5.12.1\jna-platform-5.12.1.jar;D:\Games\aloneg\libraries\net\java\dev\jna\jna\5.12.1\jna-5.12.1.jar;D:\Games\aloneg\libraries\net\sf\jopt-simple\jopt-simple\5.0.4\jopt-simple-5.0.4.jar;D:\Games\aloneg\libraries\org\apache\commons\commons-compress\1.21\commons-compress-1.21.jar;D:\Games\aloneg\libraries\org\apache\commons\commons-lang3\3.12.0\commons-lang3-3.12.0.jar;D:\Games\aloneg\libraries\org\apache\httpcomponents\httpclient\4.5.13\httpclient-4.5.13.jar;D:\Games\aloneg\libraries\org\apache\httpcomponents\httpcore\4.4.15\httpcore-4.4.15.jar;D:\Games\aloneg\libraries\org\apache\logging\log4j\log4j-api\2.19.0\log4j-api-2.19.0.jar;D:\Games\aloneg\libraries\org\apache\logging\log4j\log4j-core\2.19.0\log4j-core-2.19.0.jar;D:\Games\aloneg\libraries\org\apache\logging\log4j\log4j-slf4j2-impl\2.19.0\log4j-slf4j2-impl-2.19.0.jar;D:\Games\aloneg\libraries\org\joml\joml\1.10.5\joml-1.10.5.jar;D:\Games\aloneg\libraries\org\lwjgl\lwjgl-glfw\3.3.1\lwjgl-glfw-3.3.1.jar;D:\Games\aloneg\libraries\org\lwjgl\lwjgl-glfw\3.3.1\lwjgl-glfw-3.3.1-natives-windows.jar;D:\Games\aloneg\libraries\org\lwjgl\lwjgl-glfw\3.3.1\lwjgl-glfw-3.3.1-natives-windows-arm64.jar;D:\Games\aloneg\libraries\org\lwjgl\lwjgl-glfw\3.3.1\lwjgl-glfw-3.3.1-natives-windows-x86.jar;D:\Games\aloneg\libraries\org\lwjgl\lwjgl-jemalloc\3.3.1\lwjgl-jemalloc-3.3.1.jar;D:\Games\aloneg\libraries\org\lwjgl\lwjgl-jemalloc\3.3.1\lwjgl-jemalloc-3.3.1-natives-windows.jar;D:\Games\aloneg\libraries\org\lwjgl\lwjgl-jemalloc\3.3.1\lwjgl-jemalloc-3.3.1-natives-windows-arm64.jar;D:\Games\aloneg\libraries\org\lwjgl\lwjgl-jemalloc\3.3.1\lwjgl-jemalloc-3.3.1-natives-windows-x86.jar;D:\Games\aloneg\libraries\org\lwjgl\lwjgl-openal\3.3.1\lwjgl-openal-3.3.1.jar;D:\Games\aloneg\libraries\org\lwjgl\lwjgl-openal\3.3.1\lwjgl-openal-3.3.1-natives-windows.jar;D:\Games\aloneg\libraries\org\lwjgl\lwjgl-openal\3.3.1\lwjgl-openal-3.3.1-natives-windows-arm64.jar;D:\Games\aloneg\libraries\org\lwjgl\lwjgl-openal\3.3.1\lwjgl-openal-3.3.1-natives-windows-x86.jar;D:\Games\aloneg\libraries\org\lwjgl\lwjgl-opengl\3.3.1\lwjgl-opengl-3.3.1.jar;D:\Games\aloneg\libraries\org\lwjgl\lwjgl-opengl\3.3.1\lwjgl-opengl-3.3.1-natives-windows.jar;D:\Games\aloneg\libraries\org\lwjgl\lwjgl-opengl\3.3.1\lwjgl-opengl-3.3.1-natives-windows-arm64.jar;D:\Games\aloneg\libraries\org\lwjgl\lwjgl-opengl\3.3.1\lwjgl-opengl-3.3.1-natives-windows-x86.jar;D:\Games\aloneg\libraries\org\lwjgl\lwjgl-stb\3.3.1\lwjgl-stb-3.3.1.jar;D:\Games\aloneg\libraries\org\lwjgl\lwjgl-stb\3.3.1\lwjgl-stb-3.3.1-natives-windows.jar;D:\Games\aloneg\libraries\org\lwjgl\lwjgl-stb\3.3.1\lwjgl-stb-3.3.1-natives-windows-arm64.jar;D:\Games\aloneg\libraries\org\lwjgl\lwjgl-stb\3.3.1\lwjgl-stb-3.3.1-natives-windows-x86.jar;D:\Games\aloneg\libraries\org\lwjgl\lwjgl-tinyfd\3.3.1\lwjgl-tinyfd-3.3.1.jar;D:\Games\aloneg\libraries\org\lwjgl\lwjgl-tinyfd\3.3.1\lwjgl-tinyfd-3.3.1-natives-windows.jar;D:\Games\aloneg\libraries\org\lwjgl\lwjgl-tinyfd\3.3.1\lwjgl-tinyfd-3.3.1-natives-windows-arm64.jar;D:\Games\aloneg\libraries\org\lwjgl\lwjgl-tinyfd\3.3.1\lwjgl-tinyfd-3.3.1-natives-windows-x86.jar;D:\Games\aloneg\libraries\org\lwjgl\lwjgl\3.3.1\lwjgl-3.3.1.jar;D:\Games\aloneg\libraries\org\lwjgl\lwjgl\3.3.1\lwjgl-3.3.1-natives-windows.jar;D:\Games\aloneg\libraries\org\lwjgl\lwjgl\3.3.1\lwjgl-3.3.1-natives-windows-arm64.jar;D:\Games\aloneg\libraries\org\lwjgl\lwjgl\3.3.1\lwjgl-3.3.1-natives-windows-x86.jar;D:\Games\aloneg\libraries\org\slf4j\slf4j-api\2.0.1\slf4j-api-2.0.1.jar;D:\Games\aloneg\libraries\net\fabricmc\tiny-mappings-parser\0.3.0+build.17\tiny-mappings-parser-0.3.0+build.17.jar;D:\Games\aloneg\libraries\net\fabricmc\sponge-mixin\0.12.5+mixin.0.8.5\sponge-mixin-0.12.5+mixin.0.8.5.jar;D:\Games\aloneg\libraries\net\fabricmc\tiny-remapper\0.8.2\tiny-remapper-0.8.2.jar;D:\Games\aloneg\libraries\net\fabricmc\access-widener\2.1.0\access-widener-2.1.0.jar;D:\Games\aloneg\libraries\org\ow2\asm\asm\9.6\asm-9.6.jar;D:\Games\aloneg\libraries\org\ow2\asm\asm-analysis\9.6\asm-analysis-9.6.jar;D:\Games\aloneg\libraries\org\ow2\asm\asm-commons\9.6\asm-commons-9.6.jar;D:\Games\aloneg\libraries\org\ow2\asm\asm-tree\9.6\asm-tree-9.6.jar;D:\Games\aloneg\libraries\org\ow2\asm\asm-util\9.6\asm-util-9.6.jar;D:\Games\aloneg\libraries\net\fabricmc\intermediary\1.20.1\intermediary-1.20.1.jar;D:\Games\aloneg\libraries\net\fabricmc\fabric-loader\0.14.24\fabric-loader-0.14.24.jar;D:\Games\aloneg\versions\flandrebakapack-1.20.1\flandrebakapack-1.20.1.jar" ^
-DFabricMcEmu=net.minecraft.client.main.Main ^
-Xmn256m ^
-Xmx4096m ^
-Dlog4j2.formatMsgNoLookups=true ^
--add-exports cpw.mods.bootstraplauncher/cpw.mods.bootstraplauncher=ALL-UNNAMED ^
-Doolloo.jlw.tmpdir="C:\Users\whiter\AppData\Roaming\PCL" ^
-jar "C:\Users\whiter\AppData\Roaming\PCL\JavaWrapper.jar" net.fabricmc.loader.impl.launch.knot.KnotClient ^
--username whiterasbk ^
--version flandrebakapack-1.20.1 ^
--gameDir "D:\Games\aloneg\versions\flandrebakapack-1.20.1" ^
--assetsDir "D:\Games\aloneg\assets" ^
--assetIndex 5 ^
--uuid 00000XXXXXXXXXXXXXXXXXXXXXX114514 ^
--accessToken 00000XXXXXXXXXXXXXXXXXXXXXX43124 ^
--clientId ${clientid}
--xuid ${auth_xuid} ^
--userType msa ^
--versionType PCL ^
--width 854 ^
--height 480 ^
--quickPlayPath ${quickPlayPath}
echo 游戏已退出。
pause
        """

        return proc!!
    }

}