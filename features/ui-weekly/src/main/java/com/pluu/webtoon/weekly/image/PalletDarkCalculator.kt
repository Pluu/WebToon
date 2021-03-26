package com.pluu.webtoon.weekly.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import com.pluu.webtoon.ui.model.PalletColor
import com.pluu.webtoon.utils.LoadedState
import com.pluu.webtoon.utils.preLoadImage
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
        CoroutineScope(continuation.context).launch {
            when (val loadedState = preLoadImage(context, imageUrl)) {
                is LoadedState.Success -> {
                    continuation.resume(loadPalette(loadedState.drawable))
                }
                else -> {
                    continuation.resume(defaultPalletColor())
                }
            }
        }
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
