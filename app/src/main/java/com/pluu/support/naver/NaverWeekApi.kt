package com.pluu.support.naver

import com.pluu.support.impl.AbstractWeekApi
import com.pluu.support.impl.NAV_ITEM
import com.pluu.support.impl.REQUEST_METHOD
import com.pluu.webtoon.common.Const
import com.pluu.webtoon.di.NetworkUseCase
import com.pluu.webtoon.item.Status
import com.pluu.webtoon.item.WebToonInfo
import org.jsoup.Jsoup

/**
 * 네이버 웹툰 Week API
 * Created by pluu on 2017-04-20.
 */
class NaverWeekApi(
    networkUseCase: NetworkUseCase
) : AbstractWeekApi(networkUseCase, NaverWeekApi.TITLE) {
    private val URL_VALUE = arrayOf("mon", "tue", "wed", "thu", "fri", "sat", "sun", "fin")

    private var currentPos: Int = 0

    override val naviItem: NAV_ITEM = NAV_ITEM.NAVER

    override fun parseMain(position: Int): List<WebToonInfo> {
        currentPos = position

        val doc = try {
            Jsoup.parse(requestApi())
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }

        val pattern = "(?<=titleId=)\\d+".toRegex()
        return doc.select("#pageList a")
            .mapNotNull { element ->
                pattern.find(element.attr("href"))?.let {
                    WebToonInfo(it.value).apply {
                        title = element.select(".toon_name").text()
                        image = element.select("img").first().attr("src")
                        status = when {
                            element.select("em[class=badge badge_up]").isNotEmpty() -> Status.UPDATE // 최근 업데이트
                            element.select("em[class=badge badge_break]").isNotEmpty() -> Status.BREAK // 휴재
                            else -> Status.NONE
                        }
                        isAdult = !element.select("em[class=badge badge_adult]").isEmpty()
                        writer = element.select(".sub_info").first().text()
                        rate = Const.getRateNameByRate(element.select(".txt_score").text())
                        updateDate = element.select("span[class=if1]").text()
                    }
                }
            }
    }

    override val method: REQUEST_METHOD = REQUEST_METHOD.GET

    override val url: String = "https://m.comic.naver.com/webtoon/weekday.nhn"

    override val params: Map<String, String>
        get() = hashMapOf("week" to URL_VALUE[currentPos])

    companion object {
        private val TITLE = arrayOf("월", "화", "수", "목", "금", "토", "일", "완결")
    }
}
