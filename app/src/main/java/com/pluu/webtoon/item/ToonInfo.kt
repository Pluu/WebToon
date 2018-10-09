package com.pluu.webtoon.item

import android.os.Parcelable
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
    var isFavorite: Boolean = false
    var isLock: Boolean = false
}
