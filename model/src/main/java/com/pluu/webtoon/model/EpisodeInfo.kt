package com.pluu.webtoon.model

import com.pluu.webtoon.model.utils.parseNavigationValue
import com.pluu.webtoon.model.utils.toNavigationValue
import java.io.Serializable

typealias ToonId = String
typealias EpisodeId = String

@kotlinx.serialization.Serializable
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

    companion object {
        fun toNavigationValue(value: EpisodeInfo): String =
            value.toNavigationValue()

        fun parseNavigationValue(value: String): EpisodeInfo =
            value.parseNavigationValue()
    }
}

@kotlinx.serialization.Serializable
sealed class LandingInfo : Serializable {
    data object Detail : LandingInfo()

    data class Browser(
        val url: String
    ) : LandingInfo()
}
