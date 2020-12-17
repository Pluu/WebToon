package com.pluu.webtoon.ui.model

import java.io.Serializable

data class PalletColor(
    val darkVibrantColor: Int,
    val darkMutedColor: Int,
    val lightMutedColor: Int
) : Serializable