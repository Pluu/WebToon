package com.pluu.webtoon.utils

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.bumptech.glide.load.resource.gif.GifDrawable

fun Drawable.toGlideBitmap(): Bitmap? {
    return when (this) {
        is BitmapDrawable -> this.bitmap
        is GifDrawable -> this.firstFrame
        else -> null
    }
}