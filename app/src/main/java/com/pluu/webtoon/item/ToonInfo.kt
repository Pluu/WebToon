package com.pluu.webtoon.item

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
class ToonInfo(
    val id: String,
    val title: String,
    val image: String,
    val writer: String = "",
    val rate: String = "",
    val updateDate: String = "",
    val status: Status = Status.NONE,
    val isAdult: Boolean = false
) : Parcelable {
    @IgnoredOnParcel
    var isFavorite: Boolean = false
    @IgnoredOnParcel
    var isLock: Boolean = false
}
