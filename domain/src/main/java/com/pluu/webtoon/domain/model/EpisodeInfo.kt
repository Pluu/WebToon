package com.pluu.webtoon.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EpisodeInfo(
    val id: String,
    val toonId: String,
    val title: String,
    val image: String,
    val updateDate: String = "",
    val status: Status = Status.NONE,
    val rate: String = "",
    private val isLoginNeed: Boolean = false
) : Parcelable {

    @IgnoredOnParcel
    var isRead: Boolean = false

    @IgnoredOnParcel
    val isLock: Boolean = isLoginNeed
}
