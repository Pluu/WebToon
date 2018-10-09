@file:Suppress("NOTHING_TO_INLINE")

package com.pluu.webtoon.utils

import okhttp3.FormBody
import okhttp3.Request

inline fun buildRequest(builderAction: Request.Builder.() -> Unit): Request =
    Request.Builder().apply(builderAction).build()

inline fun Map<String, String>.toFormBody(): FormBody {
    return FormBody.Builder().also { builder ->
        for ((k, v) in this) {
            builder.add(k, v)
        }
    }.build()
}
