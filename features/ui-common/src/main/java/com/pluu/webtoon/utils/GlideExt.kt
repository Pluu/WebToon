package com.pluu.webtoon.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.PreloadTarget
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

fun Drawable.toGlideBitmap(): Bitmap? {
    return when (this) {
        is BitmapDrawable -> this.bitmap
        is GifDrawable -> this.firstFrame
        else -> null
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
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
        .load(imageUrl.toAgentGlideUrl())
        .listener(
            onReady = {
                if (cont.isCompleted) {
                    return@listener
                }
                cont.resume(LoadedState.Success(it)) {
                    requestManager.clear(target)
                }
            },
            onFailed = {
                if (cont.isCompleted) {
                    return@listener
                }
                cont.resume(LoadedState.Error(it)) {
                    requestManager.clear(target)
                }
            }
        )
        .into(target)
}

sealed class LoadedState {
    object Loading : LoadedState()
    class Success(val drawable: Drawable) : LoadedState()
    class Error(val throwable: Throwable) : LoadedState()
}