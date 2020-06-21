package com.pluu.core.utils

fun <T> lazyNone(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)
