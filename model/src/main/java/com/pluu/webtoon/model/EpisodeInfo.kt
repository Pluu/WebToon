package com.pluu.webtoon.model

import java.io.Serializable

typealias ToonId = String
typealias EpisodeId = String

data class EpisodeInfo(
    val id: EpisodeId,
    val toonId: ToonId,
    val title: String,
    val image: String,
    val updateDate: String = "",
    val status: Status = Status.NONE,
    val rate: String = "",
    private val isLoginNeed: Boolean = false,
    val landingInfo: LandingInfo = LandingInfo.Detail
) : Serializable {
    val isLock: Boolean = isLoginNeed
}

sealed class LandingInfo : Serializable {
    object Detail : LandingInfo()

    data class Browser(
        val url: String
    ) : LandingInfo()
}
