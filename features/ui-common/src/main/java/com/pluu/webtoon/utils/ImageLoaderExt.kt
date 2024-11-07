package com.pluu.webtoon.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import coil.imageLoader
import coil.request.ImageRequest
import coil.size.Size
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

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
    CoroutineScope(Dispatchers.IO).launch(
        CoroutineExceptionHandler { _, t ->
            cont.resume(LoadedState.Error(t))
        }
    ) {
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .headers(userAgentHeader)
            .size(Size.ORIGINAL)
            .allowHardware(false) // Disable hardware bitmaps.
            .build()
        val drawable = context.imageLoader.execute(request).drawable
        if (drawable != null) {
            cont.resume(LoadedState.Success(drawable))
        } else {
            cont.resume(LoadedState.Error(IllegalStateException("Unknown Exception")))
        }
    }
}

sealed class LoadedState {
    data object Loading : LoadedState()
    class Success(val drawable: Drawable) : LoadedState()
    class Error(val throwable: Throwable) : LoadedState()
}