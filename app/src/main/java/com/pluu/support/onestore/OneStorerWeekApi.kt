package com.pluu.support.onestore

import android.content.Context
import com.pluu.kotlin.asSequence
import com.pluu.kotlin.toFormatString
import com.pluu.support.impl.AbstractWeekApi
import com.pluu.support.impl.NAV_ITEM
import com.pluu.support.impl.REQUEST_METHOD
import com.pluu.webtoon.item.Status
import com.pluu.webtoon.item.WebToonInfo
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

/**
 * TStore 웹툰 Week Api
 * Created by pluu on 2017-04-27.
 */
class OneStorerWeekApi(context: Context) : AbstractWeekApi(context, OneStorerWeekApi.TITLE) {
    private var currentPos = 0
    private var page = 0
    private var startKey: String? = null

    override val naviItem: NAV_ITEM = NAV_ITEM.ONE_STORE

    override fun parseMain(position: Int): List<WebToonInfo> {
        currentPos = position + 1

        val array: JSONArray = try {
            JSONArray().apply {
                var hasNext: Boolean
                do {
                    val webtoonVO: JSONObject =
                        JSONObject(requestApi()).optJSONObject("webtoonVO") ?: return@apply
                    hasNext = webtoonVO.optString("hasNext") == "Y"
                    startKey = webtoonVO.optString("startKey")

                    webtoonVO.optJSONArray("webtoonList")
                        ?.asSequence()
                        ?.forEach {
                            put(it)
                        }
                    page++
                } while (hasNext)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }

        val currentDate = Date().toFormatString("yyyyMMdd")

        return array.asSequence().map {
            WebToonInfo(it.optString("channelId")).apply {
                title = it.optString("prodNm")
                image = it.optString("filePos")
                writer = it.optString("artistNm")
                updateDate = it.optString("updateDate")
                status = when (it.optString("statusCd")) {
                    "continue" -> if (it.optString("updateDate") == currentDate) Status.UPDATE else Status.NONE
                    "rest" -> Status.BREAK
                    else -> Status.NONE
                }
                isAdult = it.optString("intentProdGrdCd") == "4"
            }
        }.toList()
    }

    override val method: REQUEST_METHOD = REQUEST_METHOD.POST

    override val url: String
        get() = when (page) {
            0 -> "http://m.onestore.co.kr/mobilepoc/webtoon/weekdayListDetail.omp"
            else -> "http://m.onestore.co.kr/mobilepoc/webtoon/weekdayListMore.omp"
        }

    override val params: Map<String, String>
        get() = hashMapOf(
            "weekday" to currentPos.toString()
        ).apply {
            startKey?.let {
                "startKey" to it
            }
        }

    companion object {
        private val TITLE = arrayOf("월", "화", "수", "목", "금", "토", "T툰")
    }
}
