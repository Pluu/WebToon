package com.pluu.support.ktoon

import com.pluu.support.impl.AbstractWeekApi
import com.pluu.support.impl.NAV_ITEM
import com.pluu.support.impl.REQUEST_METHOD
import com.pluu.webtoon.di.NetworkModule
import com.pluu.webtoon.item.Status
import com.pluu.webtoon.item.WebToonInfo
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * 올레 웹툰 Week API
 * Created by pluu on 2017-04-22.
 */
class OllehWeekApi(
    networkModule: NetworkModule
) : AbstractWeekApi(networkModule, OllehWeekApi.TITLE) {
    override val naviItem: NAV_ITEM = NAV_ITEM.KTOON

    override fun parseMain(position: Int): List<WebToonInfo> {
        val weekly = try {
            Jsoup.parse(requestApi()).select("div[class=list week_all] .inner")
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }

        val dataPosition = weekly.select("h4")
            .indexOfFirst { it.text() == TITLE[position] }

        if (dataPosition == -1) return emptyList()

        return weekly[dataPosition].select("li")
            .mapNotNull { transform(it) }
    }

    private fun transform(it: Element): WebToonInfo? {
        val directLink = it.select(".link").attr("href")
        val toonId = "(?<=worksseq=).+".toRegex().find(directLink) ?: return null
        return WebToonInfo(toonId.value).apply {
            val info = it.select(".info")
            title = info.select("strong").text()
            image = it.select(".thumb img").attr("src")

            status = when {
                info.select(".ico_up").isNotEmpty() -> Status.UPDATE  // 최근 업데이트트
                info.select(".ico_break").isNotEmpty() -> Status.BREAK // 휴재
                else -> Status.NONE
            }

            isAdult = info.select("ico_adult").isNotEmpty()
            link = directLink
        }
    }

    override val url = "https://www.myktoon.com/web/webtoon/works_list.kt"

    override val method: REQUEST_METHOD = REQUEST_METHOD.GET

    override val headers: Map<String, String> = emptyMap()

    override val params: Map<String, String> = emptyMap()

    companion object {
        private val TITLE = arrayOf("월", "화", "수", "목", "금", "토", "일")
    }
}
