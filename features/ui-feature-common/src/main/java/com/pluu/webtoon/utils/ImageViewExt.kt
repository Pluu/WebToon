@file:Suppress("NOTHING_TO_INLINE")

package com.pluu.webtoon.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.pluu.wetoon.ui.R

const val userAgent =
    "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1"

inline fun ImageView.loadUrlOriginal(
    url: String?
) {
    loadUrl(
        url = url,
        isOriginal = true,
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
        ready = ready,
        fail = fail
    )
}

inline fun ImageView.loadUrl(
    url: String?,
    isOriginal: Boolean = false,
    @DrawableRes errorImageResId: Int,
    crossinline ready: () -> Unit = {},
    crossinline fail: () -> Unit = {}
) {
    if (url?.isNotEmpty() == true) {
        Glide.with(context)
            .load(url.toGlideUrl())
            .apply {
                if (!isOriginal) {
                    centerCrop()
                }
            }
            .error(errorImageResId)
            .listener(object : RequestListener<Drawable> {
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    ready.invoke()
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    fail.invoke()
                    return false
                }
            })
            .into(this)
    } else {
        Glide.with(context)
            .load(errorImageResId)
            .apply {
                if (!isOriginal) {
                    centerCrop()
                }
            }
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .override(Target.SIZE_ORIGINAL)
            .into(this)
    }
}

fun String.toGlideUrl(): GlideUrl = GlideUrl(
    this, LazyHeaders.Builder()
        .addHeader("User-Agent", userAgent)
        .build()
)
