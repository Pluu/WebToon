package com.pluu.webtoon.utils

import coil.request.ImageRequest

const val userAgent =
    "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1"

fun ImageRequest.Builder.applyAgent() = apply {
    addHeader("User-Agent", userAgent)
}
