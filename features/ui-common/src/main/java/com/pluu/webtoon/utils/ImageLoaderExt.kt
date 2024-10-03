package com.pluu.webtoon.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.PreloadTarget
import com.bumptech.glide.request.target.Target
import kotlinx.coroutines.suspendCancellableCoroutine

fun Drawable.toLoaderBitmap(): Bitmap? {
    return when (this) {
        is BitmapDrawable -> this.bitmap
        else -> null
    }
}

suspend fun preLoadImage(
    context: Context,
    imageUrl: String
): LoadedState = suspendCancellableCoroutine { cont ->
    val requestManager = Glide.with(context)
    val target = PreloadTarget.obtain<Drawable>(
        requestManager,
        PreloadTarget.SIZE_ORIGINAL,
        PreloadTarget.SIZE_ORIGINAL
    )

    requestManager
        .load(imageUrl.glideUrl())
        .addListener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>,
                isFirstResource: Boolean
            ): Boolean {
                if (cont.isCompleted) {
                    return false
                }
                cont.resume(
                    LoadedState.Error(e ?: IllegalStateException("Unknown Exception"))
                ) { _, _, _ ->
                    requestManager.clear(target)
                }
                return false
            }

            override fun onResourceReady(
                resource: Drawable,
                model: Any,
                target: Target<Drawable>?,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                if (cont.isCompleted) {
                    return false
                }
                cont.resume(
                    LoadedState.Success(resource)
                ) { _, _, _ ->
                    requestManager.clear(target)
                }
                return false
            }
        })
        .into(target)
}

sealed class LoadedState {
    data object Loading : LoadedState()
    class Success(val drawable: Drawable) : LoadedState()
    class Error(val throwable: Throwable) : LoadedState()
}