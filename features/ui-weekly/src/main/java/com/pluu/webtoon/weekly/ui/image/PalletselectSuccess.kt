package com.pluu.webtoon.weekly.ui.image

import android.graphics.Bitmap
import android.graphics.Color
import androidx.palette.graphics.Palette
import com.pluu.webtoon.ui.model.PalletColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun suspendLoadDarkColor(bitmap: Bitmap): PalletColor {
    return withContext(Dispatchers.Default) {
        val p = Palette.from(bitmap).generate()
        PalletColor(
            p.getDarkVibrantColor(Color.BLACK),
            p.getDarkMutedColor(Color.BLACK),
            Color.WHITE
        )
    }
}
