package com.pluu.webtoon.model

import java.io.Serializable

data class ToonInfo(
    val id: String,
    val title: String,
    val image: String,
    val backgroundColor: String = "",
    val writer: String = "",
    val rate: Double = 0.0,
    val updateDate: String = "",
    val status: Status = Status.NONE,
    val isAdult: Boolean = false
) : Serializable {
    var isLock: Boolean = false
}

data class ToonInfoWithFavorite(
    val info: ToonInfo,
    val isFavorite: Boolean = false
) : Serializable {
    val id = info.id
}
