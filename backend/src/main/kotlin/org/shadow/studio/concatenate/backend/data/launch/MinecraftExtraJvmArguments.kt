package org.shadow.studio.concatenate.backend.data.launch

data class MinecraftExtraJvmArguments(
    val initialJavaHeapSize: String? = null,
    val maximumJavaHeapSize: String? = null,
    val theYoungGenerationSize: String? = null,
    val useG1GC: Boolean = false,
    val useAdaptiveSizePolicy: Boolean = false,
    val omitStacktraceInFastThrow: Boolean = false,
    val fileEncoding: String? = null,
    val stdoutEncoding: String? = null,
    val stderrEncoding: String? = null
)