package com.pluu.support.kakao

import com.pluu.support.impl.AbstractWeekApi
import com.pluu.support.impl.NAV_ITEM
import com.pluu.support.impl.REQUEST_METHOD
import com.pluu.webtoon.di.NetworkModule
import com.pluu.webtoon.item.Status
import com.pluu.webtoon.item.WebToonInfo
import org.jsoup.Jsoup

/**
 * 카카오 페이지 웹툰 Week API
 * Created by pluu on 2017-04-25.
 */
class KakaoWeekApi(
    networkModule: NetworkModule
) : AbstractWeekApi(networkModule, KakaoWeekApi.TITLE) {
    override val url = "http://page.kakao.com/main/ajaxCallWeeklyList"
    private var currentPos: Int = 0

    override val naviItem: NAV_ITEM = NAV_ITEM.KAKAOPAGE

    override fun parseMain(position: Int): List<WebToonInfo> {
        this.currentPos = position

        val doc = try {
            Jsoup.parse(requestApi())
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }

        val pattern = "(?<=/home/)\\d+(?=\\?categoryUid)".toRegex()
        return doc.select(".list")
            .mapNotNull { element ->
                pattern.find(element.select("a").attr("href"))?.let {
                    WebToonInfo(it.value).apply {
                        title = element.select(".title").first().text()
                        image = element.select(".thumbnail img").last().attr("src")

                        if (element.select(".badgeImg").isNotEmpty()) {
                            status = Status.UPDATE
                        }
                        writer = element.select(".info").text().split("•").last().trim()
                    }
                }
            }
    }

    override val method: REQUEST_METHOD = REQUEST_METHOD.GET

    override val params: Map<String, String>
        get() = mapOf(
            "navi" to "1",
            "day" to (currentPos + 1).toString(),
            "inkatalk" to "0",
            "categoryUid" to "10",
            "subCategoryUid" to "1000"
        )

    companion object {
        private val TITLE = arrayOf("월", "화", "수", "목", "금", "토", "일")
    }
}
