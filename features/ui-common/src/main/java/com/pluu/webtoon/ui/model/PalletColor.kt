package com.pluu.webtoon.ui.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PalletColor(
    val darkVibrantColor: Int,
    val darkMutedColor: Int,
    val lightMutedColor: Int
) : Parcelable