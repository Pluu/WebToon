package com.pluu.support.naver

import android.content.Context
import com.pluu.support.impl.AbstractEpisodeApi
import com.pluu.support.impl.REQUEST_METHOD
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

        val response: String = try {
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
        val pattern = "(?<=no=)\\d+".toRegex()

        return doc.select(".lst a")
            .mapNotNull { element ->
                pattern.find(element.attr("href"))?.let {
                    Episode(info, it.value).apply {
                        episodeTitle = element.select(".toon_name").text()
                        image = element.select("img").first().attr("src")
                        parseToonInfo(element, this)
                    }
                }
            }
    }

    private fun parseToonInfo(doc: Element, episode: Episode) {
        val info = doc.select(".toon_info")
        episode.rate = info.select("span[class=if1 st_r]").text()
        episode.status = when {
            info.select(".aside_info .ico_up").isNotEmpty() -> Status.UPDATE  // 최근 업데이트
            info.select(".aside_info .ico_break").isNotEmpty() -> Status.BREAK  // 휴재
            else -> Status.NONE
        }
    }

    private fun parsePage(doc: Document): String? {
        val nextPage = doc.select(".paging_type2 .btn_next")
        return when (nextPage.isNotEmpty()) {
            true -> pageNo.toString()
            else -> null
        }
    }

    override fun moreParseEpisode(item: EpisodePage) = item.nextLink

    override fun getFirstEpisode(item: Episode) = try {
        Episode(item).apply { episodeId = "1" }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    override fun init() {
        super.init()
        pageNo = 1
    }

    override val method: REQUEST_METHOD = REQUEST_METHOD.GET

    override val url: String
        get() = "http://m.comic.naver.com/webtoon/list.nhn?titleId=$webToonId&page=$pageNo"

}
