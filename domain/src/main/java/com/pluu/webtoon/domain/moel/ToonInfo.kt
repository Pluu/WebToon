package com.pluu.webtoon.domain.moel

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
class ToonInfo(
    val id: String,
    val title: String,
    val image: String,
    val writer: String = "",
    val rate: Double = 0.0,
    val updateDate: String = "",
    val status: Status = Status.NONE,
    val isAdult: Boolean = false,
    var isFavorite: Boolean = false
) : Parcelable {
    @IgnoredOnParcel
    var isLock: Boolean = false
}
