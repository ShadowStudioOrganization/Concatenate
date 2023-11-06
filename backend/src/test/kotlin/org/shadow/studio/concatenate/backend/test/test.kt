package org.shadow.studio.concatenate.backend.test


import org.apache.maven.repository.internal.MavenRepositorySystemUtils
import org.eclipse.aether.DefaultRepositorySystemSession
import org.eclipse.aether.RepositorySystem
import org.eclipse.aether.RepositorySystemSession
import org.eclipse.aether.artifact.DefaultArtifact
import org.eclipse.aether.collection.CollectRequest
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory
import org.eclipse.aether.graph.Dependency
import org.eclipse.aether.graph.DependencyFilter
import org.eclipse.aether.impl.DefaultServiceLocator
import org.eclipse.aether.repository.LocalRepository
import org.eclipse.aether.repository.Proxy
import org.eclipse.aether.repository.RemoteRepository
import org.eclipse.aether.resolution.DependencyRequest
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory
import org.eclipse.aether.spi.connector.transport.TransporterFactory
import org.eclipse.aether.transport.http.HttpTransporterFactory
import org.shadow.studio.concatenate.backend.util.jsonObjectConvGet
import org.shadow.studio.concatenate.backend.util.parseJson
import java.io.File
import java.io.InputStream


fun main() {

    val v = jsonObjectConvGet {
        parseJson("")["properties"][0]["value"]
    }


//    val split = File("C:\\Users\\whiter\\Desktop\\cp.txt").readText().split(";")
//
//    split.forEach {
//        println(it)
//    }
//
//    testMavenResolver()

//    val json = getResourceAsString("flandrebakapack-1.20.1.json")
//    val mapper = jacksonObjectMapper()
//    val map: Map<String, Any> = mapper.readValue(json)
////    val k = mappingGameArguments(map, mapOf("auth_player_name" to "whiterasbk", "quickPlayPath" to "1"))
////    println(k.joinToString(" "))
////    val j = mappingJvmArguments(map, mapOf())
////    println(j.joinToString(" "))
//
//    JsonUtilScope.run {
//        val anies = map["libraries"] as List<*>
//        for (i in  anies) {
//            println(i["name"])
//        }
//    }


}

private fun newRepositorySystemSession(system: RepositorySystem): RepositorySystemSession {
    val session = DefaultRepositorySystemSession()
    val localRepo = LocalRepository("backend/src/test/resources/")
    println(system.newLocalRepositoryManager(session, localRepo))
    session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo))
    return session
}

private fun testMavenResolver() {
    val locator = MavenRepositorySystemUtils.newServiceLocator()
    locator.addService(RepositoryConnectorFactory::class.java, BasicRepositoryConnectorFactory::class.java)
    locator.addService(TransporterFactory::class.java, HttpTransporterFactory::class.java)
    val repository = locator.getService(RepositorySystem::class.java)
    val session = MavenRepositorySystemUtils.newSession()
    session.checksumPolicy = "fail"

    session.localRepositoryManager = repository.newLocalRepositoryManager(
        session, LocalRepository("backend/src/test/resources")
    )

    println(session.localRepositoryManager.getPathForLocalArtifact(DefaultArtifact("net.fabricmc:tiny-remapper:0.8.2")))

    val dependencies: MutableList<Dependency> = mutableListOf()
    val defaultArtifact = DefaultArtifact("net.fabricmc:tiny-remapper:0.8.2")
    val dependency = Dependency(defaultArtifact, null)
    dependencies.add(dependency)

    val proxy = Proxy("socket5", "127.0.0.1", 7890)

    val repositories = repository.newResolutionRepositories(
        session,
        listOf(RemoteRepository.Builder("fabricmc", "default", "https://maven.fabricmc.net")
             .setProxy(proxy)
            .build(),
            RemoteRepository.Builder("jitpack", null, "https://www.jitpack.io").setProxy(proxy)
                .build())
    )

    val dependencyFilter = DependencyFilter { _, _ ->  true }

    repository.resolveDependencies(
        session, DependencyRequest(
            CollectRequest(
                null as Dependency?, dependencies, repositories
            ), dependencyFilter
        )
    )



}



fun getResourceAsStream(path: String): InputStream {
    val inputStream = ::getResourceAsString.javaClass.classLoader.getResourceAsStream(path)

    if (inputStream != null) {
        return inputStream
    } else {
        error("path $path not found")
    }
}

fun getResourceAsBytes(path: String): ByteArray = getResourceAsStream(path).use { it.readBytes() }

fun getResourceAsString(path: String): String = getResourceAsStream(path).bufferedReader().use { it.readText() }
