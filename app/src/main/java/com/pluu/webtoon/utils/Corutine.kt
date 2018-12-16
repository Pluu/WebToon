package com.pluu.webtoon.utils

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

fun CoroutineContext.launch(
    block: suspend CoroutineScope.() -> Unit
): Job = GlobalScope.launch(this) {
    block()
}
