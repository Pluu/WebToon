package com.pluu.webtoon.ui.model

import java.io.Serializable

// TODO: 추후 Parcelable로 대응
data class PalletColor(
    val darkVibrantColor: Int,
    val darkMutedColor: Int,
    val lightMutedColor: Int
) : Serializable