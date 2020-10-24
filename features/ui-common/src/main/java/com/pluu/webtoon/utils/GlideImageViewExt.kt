@file:Suppress("NOTHING_TO_INLINE")

package com.pluu.webtoon.utils

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.pluu.wetoon.ui.R

const val userAgent =
    "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1"

inline fun ImageView.loadUrlOriginal(
    url: String?
) {
    loadUrl(
        url = url,
        errorImageResId = R.drawable.ic_sentiment_very_dissatisfied_48
    )
}

inline fun ImageView.loadUrl(
    url: String?,
    crossinline ready: () -> Unit = {},
    crossinline fail: () -> Unit = {}
) {
    loadUrl(
        url = url,
        errorImageResId = R.drawable.ic_sentiment_very_dissatisfied_48,
        options = { centerCrop() },
        ready = ready,
        fail = fail
    )
}

inline fun ImageView.loadUrl(
    url: String?,
    @DrawableRes errorImageResId: Int,
    options: RequestOptions.() -> Unit = { },
    crossinline ready: () -> Unit = {},
    crossinline fail: () -> Unit = {}
) {
    if (url?.isNotEmpty() == true) {
        Glide.with(context)
            .load(url.toAgentGlideUrl())
            .apply(RequestOptions().apply(options))
            .error(errorImageResId)
            .listener(
                onReady = { ready.invoke() },
                onFailed = { fail.invoke() }
            )
            .into(this)
    } else {
        Glide.with(context)
            .load(errorImageResId)
            .apply(RequestOptions().apply(options))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .override(Target.SIZE_ORIGINAL)
            .into(this)
    }
}

fun String.toAgentGlideUrl(): GlideUrl = GlideUrl(
    this, LazyHeaders.Builder()
        .addHeader("User-Agent", userAgent)
        .build()
)
