package com.pluu.webtoon.utils

import kotlinx.coroutines.*

/**
 * Coroutine Main Dispatchers
 * @param block run Block
 */
suspend inline fun withUIDispatchers(
    crossinline block: () -> Unit
) {
    withContext(Dispatchers.Main) {
        block()
    }
}

inline fun launchWithUI(
    crossinline block: suspend CoroutineScope.() -> Unit
): Job = GlobalScope.launch(Dispatchers.Main) {
    block()
}

val uiDispatchers
    get() = Dispatchers.Main

val bgDispatchers
    get() = Dispatchers.IO
