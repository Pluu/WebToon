package com.pluu.webtoon.ui.model

import androidx.compose.ui.graphics.Color
import java.io.Serializable

data class PalletColor(
    val darkVibrantColor: Color,
    val darkMutedColor: Color,
    val lightVibrantColor: Color,
    val lightMutedColor: Color,
) : Serializable