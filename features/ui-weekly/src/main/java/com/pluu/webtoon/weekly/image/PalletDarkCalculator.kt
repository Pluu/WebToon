package com.pluu.webtoon.weekly.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette
import com.pluu.webtoon.ui.model.PalletColor
import com.pluu.webtoon.utils.LoadedState
import com.pluu.webtoon.utils.preLoadImage
import com.pluu.webtoon.utils.toLoaderBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class PalletDarkCalculator(
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
        return withContext(Dispatchers.Default) {
            val p = Palette.from(this@convertPallet).generate()
            PalletColor(
                Color(p.getDarkVibrantColor(android.graphics.Color.BLACK)),
                Color(p.getDarkMutedColor(android.graphics.Color.BLACK)),
                Color(p.getLightVibrantColor(android.graphics.Color.WHITE)),
                Color(p.getLightMutedColor(android.graphics.Color.WHITE))
            )
        }
    }

    private fun defaultPalletColor() = PalletColor(Color.Black, Color.Black, Color.White, Color.White)
}
