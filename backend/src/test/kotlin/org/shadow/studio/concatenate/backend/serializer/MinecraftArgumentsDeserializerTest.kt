package org.shadow.studio.concatenate.backend.serializer

import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.shadow.studio.concatenate.backend.data.profile.Arguments
import org.shadow.studio.concatenate.backend.data.profile.ComplexMinecraftArgument
import org.shadow.studio.concatenate.backend.data.profile.Rule
import org.shadow.studio.concatenate.backend.data.profile.RuleOS

class MinecraftArgumentsDeserializerTest {

    @Test
    fun deserialize() {
        val objectMapper = jacksonObjectMapper()

        val module = SimpleModule()
        module.addDeserializer(Arguments::class.java, MinecraftArgumentsDeserializer())
        objectMapper.registerModule(module)

        val json = """
            {
                "game": [
                    "--username",
                    {
                        "rules": [
                            {
                                "action": "allow",
                                "features": {
                                    "is_demo_user": true
                                }
                            }
                        ],
                        "value": "--demo"
                    },
                    {
                        "rules": [
                            {
                                "action": "allow",
                                "features": {
                                    "has_custom_resolution": true
                                }
                            }
                        ],
                        "value": [
                            "--width",
                            "--height"
                        ]
                    }
                ],
                "jvm": [
                    {
                        "rules": [
                            {
                                "action": "allow",
                                "os": {
                                    "name": "osx"
                                }
                            }
                        ],
                        "value": [
                            "-XstartOnFirstThread"
                        ]
                    },
                    {
                        "rules": [
                            {
                                "action": "allow",
                                "os": {
                                    "name": "windows",
                                    "version": "^10\\."
                                }
                            }
                        ],
                        "value": [
                            "-Dos.name=Windows 10",
                            "-Dos.version=10.0"
                        ]
                   },
                   {
                        "rules": [
                            {
                                "action": "allow",
                                "os": {
                                    "arch": "x86"
                                }
                            }
                        ],
                        "value": "-Xss1M"
                    },
                   "-cp"
                ]
            }
        """

        val arguments = objectMapper.readValue(json, Arguments::class.java)
        assertEquals(
            Arguments(
                game = listOf(
                    "--username",
                    ComplexMinecraftArgument(
                        rules = listOf(
                            Rule(
                                action = "allow",
                                os=null,
                                features = mapOf("is_demo_user" to true)
                            )
                        ),
                        value = "--demo"),
                    ComplexMinecraftArgument(
                        rules = listOf(
                            Rule(
                                action = "allow",
                                os = null,
                                features = mapOf("has_custom_resolution" to true)
                            )
                        ),
                        value = listOf("--width", "--height")
                    )
                ),
                jvm = listOf(
                    ComplexMinecraftArgument(
                        rules = listOf(
                            Rule(
                                action = "allow",
                                os = RuleOS(name = "osx"),
                                features=null
                            )
                        ),
                        value = listOf("-XstartOnFirstThread")
                    ),
                    ComplexMinecraftArgument(
                        rules = listOf(
                            Rule(
                                action = "allow",
                                os = RuleOS(name = "windows", version="^10\\."),
                                features=null
                            )
                        ),
                        value = listOf("-Dos.name=Windows 10", "-Dos.version=10.0")
                    ),
                    ComplexMinecraftArgument(
                        rules = listOf(
                            Rule(
                                action = "allow",
                                os = RuleOS(arch = "x86"),
                                features=null
                            )
                        ),
                        value = "-Xss1M")
                    ,
                    "-cp"
                )
            ),
            arguments
        )

    }
}