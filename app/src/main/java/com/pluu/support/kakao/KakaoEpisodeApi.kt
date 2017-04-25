package com.pluu.support.kakao

import android.content.Context
import com.pluu.support.impl.AbstractEpisodeApi
import com.pluu.support.impl.NetworkSupportApi
import com.pluu.webtoon.item.Episode
import com.pluu.webtoon.item.EpisodePage
import com.pluu.webtoon.item.WebToonInfo
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * 카카오 페이지 웹툰 Episode API
 * Created by pluu on 2017-04-25.
 */
class KakaoEpisodeApi(context: Context) : AbstractEpisodeApi(context) {

    override lateinit var url: String
    private var offset: Int = 1
    private var firstEpisode: Episode? = null

    override fun parseEpisode(info: WebToonInfo): EpisodePage {
        this.url = "http://page.kakao.com/home/${info.toonId}?categoryUid=10&subCategoryUid=1000&page=$offset&orderby=desc"

        val episodePage = EpisodePage(this)

        val doc = try {
            Jsoup.parse(requestApi())
        } catch (e: Exception) {
            e.printStackTrace()
            return episodePage
        }

        if (offset == 1) {
            firstEpisode = getFirstItem(info, doc)
        }

        episodePage.episodes = parseList(info, doc)
        if (episodePage.episodes.isNotEmpty()) {
            offset++
            episodePage.nextLink = info.toonId
        }

        return episodePage
    }

    private fun getFirstItem(info: WebToonInfo, doc: Document): Episode? {
        doc.select(".homeTopContentWrp div[class=btnBox pointer clear] span[class=btn_view shortcut viewerLinkBtn]")?.attr("data-productid")?.apply {
            return Episode(info, this)
        }
        return null
    }

    private fun parseList(info: WebToonInfo, doc: Document) =
            doc.select(".list").map {
                Episode(info, it.attr("data-productid")).apply {
                    image = it.select(".thumbnail img").attr("src")
                    episodeTitle = it.select(".title").text()
                    updateDate = it.select(".date").text()
                }
            }.toList()

    override fun moreParseEpisode(item: EpisodePage): String = item.nextLink

    override fun getFirstEpisode(item: Episode): Episode? = firstEpisode

    override fun init() {
        super.init()
        offset = 1
    }

    override val method: String
        get() = NetworkSupportApi.GET

}
