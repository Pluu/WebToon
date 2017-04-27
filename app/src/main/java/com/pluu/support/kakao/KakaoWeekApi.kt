package com.pluu.support.kakao

import android.content.Context
import com.pluu.support.impl.AbstractWeekApi
import com.pluu.support.impl.NAV_ITEM
import com.pluu.support.impl.NetworkSupportApi
import com.pluu.webtoon.item.Status
import com.pluu.webtoon.item.WebToonInfo
import org.jsoup.Jsoup

/**
 * 카카오 페이지 웹툰 Week API
 * Created by pluu on 2017-04-25.
 */
class KakaoWeekApi(context: Context) : AbstractWeekApi(context, KakaoWeekApi.TITLE) {
    override val url = "http://page.kakao.com/main/ajaxCallWeeklyList"
    private var currentPos: Int = 0

    override val naviItem: NAV_ITEM
        get() = NAV_ITEM.KAKAOPAGE

    override fun parseMain(position: Int): List<WebToonInfo> {
        this.currentPos = position

        val doc = try {
            Jsoup.parse(requestApi())
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }

        val list = mutableListOf<WebToonInfo>()
        val pattern = "(?<=/home/)\\d+(?=\\?categoryUid)".toRegex()
        doc.select(".list").forEach { it ->
            pattern.find(it.select("a").attr("href"))?.apply {
                WebToonInfo(value).apply {
                    title = it.select(".title").first().text()
                    image = it.select(".thumbnail img").last().attr("src")

                    if (it.select(".badgeImg").isNotEmpty()) {
                        status = Status.UPDATE
                    }
                    writer = it.select(".info ").text().split("•".toRegex()).dropLastWhile(String::isEmpty).toTypedArray()[1]
                    list.add(this)
                }
            }
        }
        return list
    }

    override val method: String
        get() = NetworkSupportApi.GET

    override val params: Map<String, String>
        get() = mapOf("navi" to "1",
                "day" to (currentPos + 1).toString(),
                "inkatalk" to "0",
                "categoryUid" to "10",
                "subCategoryUid" to "1000")

    companion object {
        private val TITLE = arrayOf("월", "화", "수", "목", "금", "토", "일")
    }
}
