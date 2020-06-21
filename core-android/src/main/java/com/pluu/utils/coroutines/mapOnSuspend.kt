package com.pluu.utils.coroutines

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

// 참고 1. https://github.com/Kotlin/coroutines-examples/blob/master/examples/suspendingSequence/suspendingSequence.kt
// 참고 2. https://jivimberg.io/blog/2018/05/04/parallel-map-in-kotlin/
suspend fun <A, B> Iterable<A>.mapOnSuspend(f: suspend (A) -> B): List<B> = coroutineScope {
    map { async { f(it) } }.map { it.await() }
}
