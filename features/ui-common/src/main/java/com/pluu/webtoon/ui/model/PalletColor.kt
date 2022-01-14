package com.pluu.webtoon.ui.model

import androidx.annotation.ColorInt
import java.io.Serializable

data class PalletColor(
    @ColorInt val darkVibrantColor: Int,
    @ColorInt val darkMutedColor: Int,
    @ColorInt val lightMutedColor: Int
) : Serializable