package com.pluu.support.onestore

import android.content.Context
import com.pluu.support.impl.AbstractWeekApi
import com.pluu.support.impl.NAV_ITEM
import com.pluu.support.impl.REQUEST_METHOD
import com.pluu.webtoon.item.Status
import com.pluu.webtoon.item.WebToonInfo
import org.jsoup.Jsoup

/**
 * TStore 웹툰 Week Api
 * Created by pluu on 2017-04-27.
 */
class OneStorerWeekApi(context: Context) : AbstractWeekApi(context, OneStorerWeekApi.TITLE) {
    private val ID_PATTERN =
        arrayOf("(?<=prodId=).+(?=&)".toRegex(), "(?<=goAdultAuthPage\\(').+(?=')".toRegex())

    private var currentPos: Int = 0

    override val naviItem: NAV_ITEM = NAV_ITEM.ONE_STORE

    override fun parseMain(position: Int): List<WebToonInfo> {
        currentPos = position

        val doc = try {
            Jsoup.parse(requestApi())
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }

        return doc.select(".offering-card-item")
            .mapNotNull { element ->
                val href = element.attr("href")
                ID_PATTERN
                    .mapNotNull { it.find(href) }
                    .firstOrNull()?.value?.let { id ->
                    WebToonInfo(id).apply {
                        title = element.select(".product-ti").text()
                        image = element.select(".thumbnail").attr("data-thumbackground")
                        writer = element.select(".product-writer").text()

                        val related = element.select(".product-related")
                        updateDate = related.last().text()
                        status = when {
                            element.select("em[class=.product-badge-icon product-badge-icon-01]").text() == "UP" -> Status.UPDATE
                            related.first().text() == "휴재" -> Status.BREAK
                            else -> Status.NONE
                        }
                    }
                }
            }
    }

    override val method: REQUEST_METHOD = REQUEST_METHOD.POST

    override val url: String
        get() = "http://m.onestore.co.kr/mobilepoc/webtoon/weekdayList.omp"

    override val headers: Map<String, String>
        get() = hashMapOf(
            "Referer" to "http://m.onestore.co.kr/mobilepoc/webtoon/weekdayList.omp"
        )

    override val params: Map<String, String>
        get() = hashMapOf(
            "weekday" to currentPos.toString()
        )

    companion object {
        private val TITLE = arrayOf("월", "화", "수", "목", "금", "토", "T툰")
    }
}
