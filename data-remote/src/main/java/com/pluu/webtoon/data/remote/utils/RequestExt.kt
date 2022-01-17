@file:Suppress("NOTHING_TO_INLINE")

package com.pluu.webtoon.data.remote.utils

import okhttp3.FormBody

inline fun Map<String, String>.toFormBody(): FormBody {
    return FormBody.Builder().also { builder ->
        for ((k, v) in this) {
            builder.add(k, v)
        }
    }.build()
}
