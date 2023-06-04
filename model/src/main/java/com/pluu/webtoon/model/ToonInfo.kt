package com.pluu.webtoon.model

import com.pluu.webtoon.model.utils.parseNavigationValue
import com.pluu.webtoon.model.utils.toNavigationValue
import java.io.Serializable

@kotlinx.serialization.Serializable
data class ToonInfo(
    val id: String,
    val title: String,
    val image: String,
    val backgroundColor: String = "",
    val writer: String = "",
    val rate: Double = 0.0,
    val updateDate: String = "",
    val status: Status = Status.NONE,
    val isLocked: Boolean = false
) : Serializable

@kotlinx.serialization.Serializable
data class ToonInfoWithFavorite(
    val info: ToonInfo,
    val isFavorite: Boolean = false
) : Serializable {
    val id: String = info.id

    companion object {
        fun toNavigationValue(value: ToonInfoWithFavorite): String =
            value.toNavigationValue()

        fun parseNavigationValue(value: String): ToonInfoWithFavorite =
            value.parseNavigationValue()
    }
}
