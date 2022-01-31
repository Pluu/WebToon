package com.pluu.webtoon.weekly.image

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette
import com.pluu.webtoon.ui.model.PalletColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun suspendLoadDarkColor(bitmap: Bitmap): PalletColor {
    return withContext(Dispatchers.Default) {
        val p = Palette.from(bitmap).generate()
        PalletColor(
            Color(p.getDarkVibrantColor(android.graphics.Color.BLACK)),
            Color(p.getDarkMutedColor(android.graphics.Color.BLACK)),
            Color.White
        )
    }
}