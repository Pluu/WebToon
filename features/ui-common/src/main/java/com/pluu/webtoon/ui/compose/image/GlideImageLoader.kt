package com.pluu.webtoon.ui.compose.image

import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

inline fun <T> RequestBuilder<T>.listener(
    crossinline onSuccess: (T) -> Unit,
    crossinline onFailure: (Exception) -> Unit = { }
): RequestBuilder<T> = listener(
    object : RequestListener<T> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<T>?,
            isFirstResource: Boolean
        ): Boolean {
            onFailure(e ?: Exception("Unknown Exception"))
            return true
        }

        override fun onResourceReady(
            resource: T,
            model: Any?,
            target: Target<T>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            onSuccess(resource)
            return true
        }
    }
)
