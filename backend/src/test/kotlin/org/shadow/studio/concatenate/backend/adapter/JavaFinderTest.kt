package org.shadow.studio.concatenate.backend.adapter

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class JavaFinderTest {

    @Test
    fun findJava() {
        val versionRegex = Regex("\"([0-9_]+(\\.[0-9_]+)+)\"")

        val f = versionRegex.find("java version \"16.0_1.1.2.3\" 2021-04-20")
        println(f?.groups?.get(1)?.value)

        println("java version \"16.0.1\" 2021-04-20".contains(Regex("\"[0-9_]+(\\.[0-9_]+)+\"")))
//        JavaFinder().find()
    }

    @Test
    fun testJavaExecutable(): Unit = runBlocking {


        JavaFinder().find().forEach(::println)


//        val a = JavaFinder().determineJavaExecutable("D:/Environments/java/jbr-17/bin/java.exe")
//
//        println(a.await())

    }



}