package com.pluu.webtoon.ui.model

import com.pluu.webtoon.model.utils.parseNavigationValue
import com.pluu.webtoon.model.utils.toNavigationValue
import java.io.Serializable

@kotlinx.serialization.Serializable
data class PalletColor(
    val darkVibrantColor: Long,
    val darkMutedColor: Long,
    val lightVibrantColor: Long,
    val lightMutedColor: Long,
) : Serializable {
    companion object {
        fun toNavigationValue(value: PalletColor): String =
            value.toNavigationValue()

        fun parseNavigationValue(value: String): PalletColor =
            value.parseNavigationValue()
    }
}