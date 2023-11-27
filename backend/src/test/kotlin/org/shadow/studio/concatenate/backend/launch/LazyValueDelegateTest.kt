package org.shadow.studio.concatenate.backend.launch

import org.junit.jupiter.api.Test
import org.shadow.studio.concatenate.backend.util.LazyValueDelegate

class LazyValueDelegateTest {

    @Test
    fun test() {



        var obj by LazyValueDelegate {
            println("init")
            1
        }

        println(obj)
        println(obj)
        println(obj)

        obj = 2

        println(obj)
        println(obj)
    }
}