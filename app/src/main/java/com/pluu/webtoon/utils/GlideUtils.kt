package com.pluu.webtoon.utils

import android.graphics.Bitmap
import android.graphics.drawable.TransitionDrawable
import android.widget.ImageView
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable
import com.bumptech.glide.request.target.SquaringDrawable

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
        is GlideBitmapDrawable -> lblDrawable.bitmap
        is TransitionDrawable -> convertTransitionDrawable(lblDrawable)
        is SquaringDrawable -> (lblDrawable.current as GlideBitmapDrawable).bitmap
        else -> null
    }
}

private fun convertTransitionDrawable(drawables: TransitionDrawable): Bitmap? {
    (0 until drawables.numberOfLayers - 1)
            .map { drawables.getDrawable(it) }
            .forEach {
                if (it is GlideBitmapDrawable) {
                    return it.bitmap
                } else if (it is SquaringDrawable && it.getCurrent() is GlideBitmapDrawable) {
                    return (it.getCurrent() as GlideBitmapDrawable).bitmap
                }
            }
    return null
}
