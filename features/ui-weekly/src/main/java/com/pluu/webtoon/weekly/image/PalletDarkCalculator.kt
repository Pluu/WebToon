package com.pluu.webtoon.weekly.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import com.pluu.webtoon.ui.model.PalletColor
import com.pluu.webtoon.utils.LoadedState
import com.pluu.webtoon.utils.preLoadImage
import com.pluu.webtoon.utils.toLoaderBitmap

class PalletDarkCalculator(
    private val context: Context
) {
    suspend fun calculateSwatchesInImage(
        imageUrl: String
    ): PalletColor = when (val loadedState = preLoadImage(context, imageUrl)) {
        is LoadedState.Success -> {
            loadPalette(loadedState.drawable)
        }
        else -> {
            defaultPalletColor()
        }
    }

    private suspend fun loadPalette(
        drawable: Drawable
    ): PalletColor {
        val bitmap = drawable.toLoaderBitmap()
        return if (bitmap != null) {
            runCatching {
                bitmap.convertPallet()
            }.getOrDefault(defaultPalletColor())
        } else {
            defaultPalletColor()
        }
    }

    private suspend fun Bitmap.convertPallet(): PalletColor {
        return suspendLoadDarkColor(this)
    }

    private fun defaultPalletColor() = PalletColor(Color.BLACK, Color.BLACK, Color.WHITE)
}
