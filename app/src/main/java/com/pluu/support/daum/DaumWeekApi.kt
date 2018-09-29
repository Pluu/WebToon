package com.pluu.support.daum

import com.pluu.kotlin.asSequence
import com.pluu.kotlin.isNotEmpty
import com.pluu.support.impl.AbstractWeekApi
import com.pluu.support.impl.NAV_ITEM
import com.pluu.support.impl.REQUEST_METHOD
import com.pluu.webtoon.common.Const
import com.pluu.webtoon.di.NetworkUseCase
import com.pluu.webtoon.item.BaseToonInfo
import com.pluu.webtoon.item.Status
import com.pluu.webtoon.item.WebToonInfo
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

/**
 * 다음 웹툰 Week Api
 * Created by pluu on 2017-04-20.
 */
class DaumWeekApi(
    networkUseCase: NetworkUseCase
) : AbstractWeekApi(networkUseCase, DaumWeekApi.TITLE) {
    private val URL_VALUE = arrayOf("mon", "tue", "wed", "thu", "fri", "sat", "sun")

    private var currentPos: Int = 0

    override val naviItem: NAV_ITEM = NAV_ITEM.DAUM

    override fun parseMain(position: Int): List<WebToonInfo> {
        this.currentPos = position

        val array: JSONArray? = try {
            JSONObject(requestApi())
                .optJSONObject("data")
                .optJSONArray("webtoons")
        } catch (e: Exception) {
            return emptyList()
        }

        if (array?.isNotEmpty() != true) {
            return emptyList()
        }

        val today = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())

        return array.asSequence().map { obj ->
            val lastObj = obj.optJSONObject("latestWebtoonEpisode")
            val baseInfo = BaseToonInfo(obj.optString("nickname")).apply {
                title = obj.optString("title")
            }
            WebToonInfo(baseInfo).apply {
                image = lastObj.optJSONObject("thumbnailImage").optString("url")

                val info = obj.optJSONObject("cartoon").optJSONArray("artists").optJSONObject(0)
                writer = info.optString("name")
                rate = Const.getRateNameByRate(obj.optString("averageScore")).takeIf { it != "0.0" }

                val date = lastObj.optString("dateCreated")
                updateDate =
                        "${date.substring(2, 4)}.${date.substring(4, 6)}.${date.substring(6, 8)}"
                status = when {
                    today == date.substring(0, 8) -> Status.UPDATE // 최근 업데이트
                    "Y" == obj.optString("restYn") -> Status.BREAK // 휴재
                    else -> Status.NONE
                }

                isAdult = obj.optInt("ageGrade") == 1
            }
        }.toList()
    }

    override val method: REQUEST_METHOD = REQUEST_METHOD.POST

    override val url: String = "http://m.webtoon.daum.net/data/mobile/webtoon"

    override val params: Map<String, String>
        get() = hashMapOf(
            "sort" to "update",
            "page_no" to "1",
            "week" to URL_VALUE[currentPos]
        )

    companion object {
        private val TITLE = arrayOf("월", "화", "수", "목", "금", "토", "일")
    }
}
