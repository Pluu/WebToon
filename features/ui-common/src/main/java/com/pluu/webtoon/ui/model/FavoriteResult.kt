package com.pluu.webtoon.ui.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 즐겨찾기 여부 업데이트 Pending Extra Data
 */
@Parcelize
class FavoriteResult(
    val id: String,
    val isFavorite: Boolean
) : Parcelable
