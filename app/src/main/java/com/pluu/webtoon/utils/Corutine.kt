package com.pluu.webtoon.utils

import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.android.Main
import kotlinx.coroutines.experimental.withContext

/**
 * Coroutine Main Dispatchers
 * @param block run Block
 */
suspend inline fun withMainDispatchers(crossinline block: () -> Unit) {
    withContext(Dispatchers.Main) {
        block()
    }
}
