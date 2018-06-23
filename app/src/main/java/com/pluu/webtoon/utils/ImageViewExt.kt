@file:Suppress("NOTHING_TO_INLINE")

package com.pluu.webtoon.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.pluu.webtoon.R

val userAgent =
    "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1"

inline fun ImageView.loadUrlOriginal(
    url: String?
) {
    val glideUrl = GlideUrl(
        url, LazyHeaders.Builder()
            .addHeader(
                "User-Agent", userAgent
            )
            .build()
    )

    Glide.with(context)
        .load(glideUrl)
        .apply(
            RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .override(Target.SIZE_ORIGINAL)
        )
        .into(this)
}

inline fun ImageView.loadUrl(
    url: String?,
    crossinline ready: () -> Unit = {},
    crossinline fail: () -> Unit = {}
) {
    val glideUrl = GlideUrl(
        url, LazyHeaders.Builder()
            .addHeader(
                "User-Agent", userAgent
            )
            .build()
    )

    Glide.with(context)
        .load(glideUrl)
        .apply(
            RequestOptions()
                .centerCrop()
                .error(R.drawable.ic_sentiment_very_dissatisfied_black_36dp)
        )
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
}

