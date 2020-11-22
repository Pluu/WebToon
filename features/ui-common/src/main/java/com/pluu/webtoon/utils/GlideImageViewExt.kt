@file:Suppress("NOTHING_TO_INLINE")

package com.pluu.webtoon.utils

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestOptions

const val userAgent =
    "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1"

inline fun ImageView.loadUrl(
    url: String?,
    @DrawableRes errorImageResId: Int,
    options: RequestOptions.() -> Unit = { },
    crossinline ready: () -> Unit = {},
    crossinline fail: () -> Unit = {}
) {
    Glide.with(context)
        .load(url?.toAgentGlideUrl())
        .apply(RequestOptions().apply(options))
        .error(errorImageResId)
        .listener(
            onReady = { ready.invoke() },
            onFailed = { fail.invoke() }
        )
        .into(this)
}

fun String.toAgentGlideUrl(): GlideUrl = GlideUrl(
    this, LazyHeaders.Builder()
        .addHeader("User-Agent", userAgent)
        .build()
)
