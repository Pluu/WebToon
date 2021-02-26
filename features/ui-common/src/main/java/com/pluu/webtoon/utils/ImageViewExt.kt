@file:Suppress("NOTHING_TO_INLINE")

package com.pluu.webtoon.utils

import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders

const val userAgent =
    "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1"

fun String.toAgentGlideUrl(): GlideUrl = GlideUrl(
    this, LazyHeaders.Builder()
        .addHeader("User-Agent", userAgent)
        .build()
)
