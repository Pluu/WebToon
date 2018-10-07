package com.pluu.webtoon.utils

import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.Main

/**
 * Coroutine Main Dispatchers
 * @param block run Block
 */
suspend inline fun withMainDispatchers(
    crossinline block: () -> Unit
) {
    withContext(Dispatchers.Main) {
        block()
    }
}

inline fun coroutineLaunchWithMain(
    crossinline block: suspend CoroutineScope.() -> Unit
): Job = GlobalScope.launch(Dispatchers.Main) {
    block()
}
