package com.pluu.webtoon.utils

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.TransitionDrawable
import android.widget.ImageView
import com.bumptech.glide.load.resource.gif.GifDrawable

/**
 * Glide Image Utils
 * Created by pluu on 2017-04-29.
 */
/**
 * Get Bitmap
 * @see [Base Code Url](http://dalinaum.github.io/android/2015/07/07/retrieving-bitmap-of-glide.html)
 * @return Bitmap
 */
fun glideBitmap(imgView: ImageView): Bitmap? {
    val lblDrawable = imgView.drawable
    return when (lblDrawable) {
        is BitmapDrawable -> lblDrawable.bitmap
        is TransitionDrawable -> convertTransitionDrawable(lblDrawable)
        is GifDrawable -> lblDrawable.firstFrame
        else -> null
    }
}

private fun convertTransitionDrawable(drawables: TransitionDrawable): Bitmap? {
    (0 until drawables.numberOfLayers - 1)
            .map { drawables.getDrawable(it) }
            .forEach {
                if (it is BitmapDrawable) {
                    return it.bitmap
                }
            }
    return null
}
