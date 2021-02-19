package com.pluu.webtoon.model

import java.io.Serializable

typealias EpisodeId = String

data class EpisodeInfo(
    val id: EpisodeId,
    val toonId: String,
    val title: String,
    val image: String,
    val updateDate: String = "",
    val status: Status = Status.NONE,
    val rate: String = "",
    private val isLoginNeed: Boolean = false
) : Serializable {
    val isLock: Boolean = isLoginNeed
}
