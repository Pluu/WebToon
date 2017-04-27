package com.pluu.support.tstore

import android.content.Context
import com.pluu.support.impl.AbstractWeekApi
import com.pluu.support.impl.NAV_ITEM
import com.pluu.support.impl.NetworkSupportApi
import com.pluu.webtoon.item.Status
import com.pluu.webtoon.item.WebToonInfo
import org.jsoup.Jsoup

/**
 * TStore 웹툰 Week Api
 * Created by pluu on 2017-04-27.
 */
class TStorerWeekApi(context: Context) : AbstractWeekApi(context, TStorerWeekApi.TITLE) {
    private val ID_PATTERN = "(?<=prodId=).+(?=&)".toRegex()
    private val IMG_PATTERN = "(?<=url\\(').+(?='\\);)".toRegex()
    private val DATE_PATTERN = "\\d{4}.\\d{2}.\\d{2}".toRegex()

    private var currentPos: Int = 0

    override val naviItem: NAV_ITEM
        get() = NAV_ITEM.T_STORE

    override fun parseMain(position: Int): List<WebToonInfo> {
        this.currentPos = position

        val doc = try {
            Jsoup.parse(requestApi())
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }

        val list = mutableListOf<WebToonInfo>()
        doc.select("#weekToon0" + (position + 1) + " ul li a").forEach {
            ID_PATTERN.find(it.attr("href"))?.apply {
                WebToonInfo(value).apply {
                    title = it.select(".list-item-text-title").text()
                    image = IMG_PATTERN.find(it.select("span[class=list-thumbnail-pic ebook-lazy]").attr("style"))?.value
                    writer = it.select(".list-item-text-like-info").last()?.children()?.last()?.text()
                    updateDate = DATE_PATTERN.find(it.select("span[class=list-item-text-date list-item-text-point]").text())?.value

                    if (it.select("i[class=icon-type-ktoon icon-badge-update]").isNotEmpty()) {
                        // 최근 업데이트
                        status = Status.UPDATE
                    }
                    list.add(this)
                }
            }
        }
        return list
    }

    override val method: String
        get() = NetworkSupportApi.POST

    override val url: String
        get() = "http://m.tstore.co.kr/mobilepoc/webtoon/weekdayList.omp?weekday=${currentPos + 1}"

    companion object {
        private val TITLE = arrayOf("월", "화", "수", "목", "금", "토", "T툰")
    }
}
