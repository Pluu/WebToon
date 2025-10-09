package com.pluu.webtoon.model

import java.io.Serializable

typealias ToonId = String
typealias EpisodeId = String

@kotlinx.serialization.Serializable
data class EpisodeInfo(
    val id: EpisodeId,
    val toonId: ToonId,
    val title: String,
    val toonTitle: String,
    val image: String,
    val updateDate: String = "",
    val status: Status = Status.NONE,
    val rate: String = "",
    private val isLoginNeed: Boolean = false,
    val landingInfo: LandingInfo = LandingInfo.Detail
) : Serializable {
    val isLock: Boolean = isLoginNeed
}

@kotlinx.serialization.Serializable
sealed interface LandingInfo : Serializable {
    @kotlinx.serialization.Serializable
    data object Detail : LandingInfo {
        private fun readResolve(): Any = Detail
    }

    @kotlinx.serialization.Serializable
    data class Browser(
        val url: String
    ) : LandingInfo
}
