package com.pluu.webtoon.weekly.ui.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.PreloadTarget
import com.pluu.webtoon.ui.model.PalletColor
import com.pluu.webtoon.utils.listener
import com.pluu.webtoon.utils.toAgentGlideUrl
import com.pluu.webtoon.utils.toGlideBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class PalletDarkCalculator(
    private val context: Context
) {
    suspend fun calculateSwatchesInImage(
        imageUrl: String
    ): PalletColor = suspendCancellableCoroutine { continuation ->
        val requestManager = Glide.with(context)
        requestManager
            .load(imageUrl.toAgentGlideUrl())
            .onlyRetrieveFromCache(true)
            .listener(
                onReady = {
                    CoroutineScope(continuation.context).launch {
                        continuation.resume(loadPalette(it))
                    }
                },
                onFailed = {
                    CoroutineScope(continuation.context).launch {
                        continuation.resume(defaultPalletColor())
                    }
                }
            )
            .into(
                PreloadTarget.obtain(
                    requestManager,
                    PreloadTarget.SIZE_ORIGINAL,
                    PreloadTarget.SIZE_ORIGINAL
                )
            )
    }

    private suspend fun loadPalette(
        drawable: Drawable
    ): PalletColor {
        val bitmap = drawable.toGlideBitmap()
        return bitmap?.convertPallet() ?: defaultPalletColor()
    }

    private suspend fun Bitmap.convertPallet(): PalletColor {
        return suspendLoadDarkColor(this)
    }

    private fun defaultPalletColor() = PalletColor(Color.BLACK, Color.BLACK, Color.WHITE)
}
