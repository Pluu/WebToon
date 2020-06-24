package com.pluu.webtoon.model

import java.io.Serializable

data class EpisodeInfo(
    val id: String,
    val toonId: String,
    val title: String,
    val image: String,
    val updateDate: String = "",
    val status: Status = Status.NONE,
    val rate: String = "",
    private val isLoginNeed: Boolean = false
) : Serializable {
    var isRead: Boolean = false

    val isLock: Boolean = isLoginNeed
}
