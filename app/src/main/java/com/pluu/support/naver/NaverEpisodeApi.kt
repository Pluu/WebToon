package com.pluu.support.naver

import android.content.Context
import com.pluu.support.impl.AbstractEpisodeApi
import com.pluu.support.impl.NetworkSupportApi
import com.pluu.webtoon.item.Episode
import com.pluu.webtoon.item.EpisodePage
import com.pluu.webtoon.item.Status
import com.pluu.webtoon.item.WebToonInfo
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

/**
 * 네이버 웹툰 Episode API
 * Created by pluu on 2017-04-20.
 */
class NaverEpisodeApi(context: Context) : AbstractEpisodeApi(context) {
    private var webToonId: String? = null
    private var pageNo = 1

    override fun parseEpisode(info: WebToonInfo): EpisodePage {
        webToonId = info.toonId

        val episodePage = EpisodePage(this)

        val response: String? = try {
            requestApi()
        } catch (e: Exception) {
            e.printStackTrace()
            return episodePage
        }

        val doc = Jsoup.parse(response)
        episodePage.episodes = parseList(info, doc)
        episodePage.nextLink = parsePage(doc)
        pageNo++

        return episodePage
    }

    private fun parseList(info: WebToonInfo, doc: Document): List<Episode> {
        val list = mutableListOf<Episode>()
        val pattern = "(?<=no=)\\d+".toRegex()

        try {
            for (a in doc.select(".lst a")) {
                pattern.find(a.attr("href"))?.apply {
                    Episode(info, value).apply {
                        episodeTitle = a.select(".toon_name").text()
                        image = a.select("img").first().attr("src")
                        parseToonInfo(a, this)
                        list.add(this)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return list
    }

    private fun parseToonInfo(doc: Element, episode: Episode) {
        val info = doc.select(".toon_info")
        episode.rate = info.select("span[class=if1 st_r]").text()

        if (info.select(".aside_info .ico_up").isNotEmpty()) {
            // 최근 업데이트
            episode.status = Status.UPDATE
        } else if (info.select(".aside_info .ico_break").isNotEmpty()) {
            // 휴재
            episode.status = Status.BREAK
        }
    }

    private fun parsePage(doc: Document): String? {
        val nextPage = doc.select(".paging_type2 [data-type=next]")
        return when(nextPage.isNotEmpty()) {
            true -> nextPage.attr("data-page")
            else -> null
        }
    }

    override fun moreParseEpisode(item: EpisodePage): String = item.getNextLink()

    override fun getFirstEpisode(item: Episode): Episode? {
        return try {
            Episode(item).apply { episodeId = "1" }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    override fun init() {
        super.init()
        pageNo = 1
    }

    override val method: String
        get() = NetworkSupportApi.GET

    override val url: String
        get() = HOST_URL.format(webToonId, pageNo)

    companion object {
        private val HOST_URL = "http://m.comic.naver.com/webtoon/list.nhn?titleId=%s&page=%d"
    }
}
