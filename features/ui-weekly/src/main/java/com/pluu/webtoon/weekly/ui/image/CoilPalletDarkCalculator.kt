package com.pluu.webtoon.weekly.ui.image

import android.content.Context
import android.graphics.Color
import androidx.core.graphics.drawable.toBitmap
import coil.Coil
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Scale
import com.pluu.webtoon.ui.model.PalletColor

class CoilPalletDarkCalculator(
    private val context: Context
) {
    suspend fun calculateSwatchesInImage(
        imageUrl: String
    ): PalletColor {
        val r = ImageRequest.Builder(context)
            .data(imageUrl)
            // We scale the image to cover 128px x 128px (i.e. min dimension == 128px)
            .size(128).scale(Scale.FILL)
            // Disable hardware bitmaps, since Palette uses Bitmap.getPixels()
            .allowHardware(false)
            .build()

        val bitmap = when (val result = Coil.execute(r)) {
            is SuccessResult -> result.drawable.toBitmap()
            else -> null
        }

        return bitmap?.let {
            suspendLoadDarkColor(bitmap)
        } ?: PalletColor(Color.BLACK, Color.BLACK, Color.WHITE)
    }
}
