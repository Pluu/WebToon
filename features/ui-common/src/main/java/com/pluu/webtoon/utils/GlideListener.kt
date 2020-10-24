package com.pluu.webtoon.utils

import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

inline fun <reified T> RequestBuilder<T>.listener(
    crossinline onReady: (T) -> Unit,
    crossinline onFailed: (Exception) -> Unit
) = listener(
    object : RequestListener<T> {
        override fun onResourceReady(
            resource: T,
            model: Any?,
            target: Target<T>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            onReady.invoke(resource)
            return false
        }

        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<T>?,
            isFirstResource: Boolean
        ): Boolean {
            onFailed.invoke(e ?: IllegalStateException("Unknown Exception"))
            return false
        }
    }
)