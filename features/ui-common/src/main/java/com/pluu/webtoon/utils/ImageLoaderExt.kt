package com.pluu.webtoon.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import coil.imageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.ExperimentalCoroutinesApi

fun Drawable.toLoaderBitmap(): Bitmap? {
    return when (this) {
        is BitmapDrawable -> this.bitmap
        else -> null
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun preLoadImage(
    context: Context,
    imageUrl: String
): LoadedState {
    val request = ImageRequest.Builder(context)
        .applyAgent()
        .data(imageUrl)
        .allowHardware(false)
        .build()

    return when (val result = context.imageLoader.execute(request)) {
        is SuccessResult -> {
            LoadedState.Success(result.drawable)
        }
        is ErrorResult -> {
            LoadedState.Error(result.throwable)
        }
    }
}

sealed class LoadedState {
    object Loading : LoadedState()
    class Success(val drawable: Drawable) : LoadedState()
    class Error(val throwable: Throwable) : LoadedState()
}