package com.pluu.webtoon.utils

fun <T> lazyNone(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)