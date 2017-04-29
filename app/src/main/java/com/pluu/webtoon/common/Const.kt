package com.pluu.webtoon.common

/**
 * Application Const Set
 * Created by pluu on 2017-04-29.
 */
object Const {
    val LOG_TAG = "PluuToon"
    const val EXTRA_API = "EXTRA_API"
    const val EXTRA_POS = "EXTRA_POS"
    const val EXTRA_EPISODE = "EXTRA_EPISODE"
    const val EXTRA_MAIN_COLOR = "EXTRA_MAIN_COLOR"
    const val EXTRA_STATUS_COLOR = "EXTRA_STATUS_COLOR"

    const val CONFIG_NAME = "service"

    private val RATE_FORMAT = "평점 : %.2f"

    const val MAIN_FRAG_TAG = "main_frag_tag"
    const val DETAIL_FRAG_TAG = "detail_frag_tag"

    fun getRateNameByRate(data: String) = RATE_FORMAT.format(data.toDouble())

}
