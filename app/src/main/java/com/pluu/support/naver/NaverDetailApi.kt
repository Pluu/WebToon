package com.pluu.support.naver

import android.content.Context
import com.pluu.support.impl.AbstractDetailApi
import com.pluu.support.impl.NetworkSupportApi
import com.pluu.webtoon.item.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.*

/**
 * 네이버 웹툰 상세 API
 * Created by pluu on 2017-04-20.
 */
class NaverDetailApi(context: Context) : AbstractDetailApi(context) {

    private val SHARE_URL = "http://m.comic.naver.com/webtoon/detail.nhn?titleId=%s&no=%s"
    private val SKIP_DETAIL = arrayOf(
            "http://static.naver.com/m/comic/im/txt_ads.png",
            "http://static.naver.com/m/comic/im/toon_app_pop.png")

    private var webToonId: String? = null
    private var episodeId: String? = null

    override fun parseDetail(episode: Episode): Detail {
        this.webToonId = episode.toonId
        this.episodeId = episode.episodeId

        val ret = Detail().apply {
            webtoonId = episode.toonId
            episodeId = episode.episodeId
        }

        val response: String? = try {
            requestApi()
        } catch (e: Exception) {
            e.printStackTrace()
            ret.list = ArrayList<DetailView>()
            return ret
        }

        val doc = Jsoup.parse(response)
        ret.title = doc.select("div[class=chh] span, h1[class=tit]").first().text()

        if (doc.select("div[class=viewer cuttoon]")?.isNotEmpty() ?: false) {
            // 컷툰
            parseCutToon(ret, doc)
            return ret
        }
        if (doc.select(".oz-loader")?.isNotEmpty() ?: false) {
            // osLoader
            ret.errorType = ERROR_TYPE.NOT_SUPPORT
            return ret
        }

        // 일반 웹툰
        parseNormal(ret, doc)
        return ret
    }

    private fun parseNormal(ret: Detail, doc: Document) {
        ret.list = parseDetailNormalType(doc)

        // 이전, 다음화
        doc.select(".navi_area").apply {
            select("[data-type=next]")?.attr("data-no")?.apply {
                ret.nextLink = if (isNotEmpty()) this else null
            }
            select("[data-type=prev]")?.attr("data-no")?.apply {
                ret.prevLink = if (isNotEmpty()) this else null
            }
        }
    }

    private fun parseCutToon(ret: Detail, doc: Document) {
        ret.list = parseDetailCutToonType(doc)

        // 이전, 다음화
        doc.select(".paging_wrap")?.apply {
            select("[data-type=next]")?.attr("data-no")?.apply {
                ret.nextLink = if (isNotEmpty()) this else null
            }
            select("[data-type=prev]")?.attr("data-no")?.apply {
                ret.prevLink = if (isNotEmpty()) this else null
            }
        }
    }

    private fun parseDetailCutToonType(doc: Document): List<DetailView> {
        return doc.select(".swiper-slide img.swiper-lazy")
                .filter { it -> it.hasAttr("data-categoryid") }
                .map { it -> it.attr("data-src") }
                .map { url -> DetailView.createImage(url) }
                .toList()
    }

    private fun parseDetailNormalType(doc: Document): List<DetailView> {
        val list = mutableListOf<DetailView>()
        var path: String

        for (item in doc.select("#toonLayer li img")) {
            path = item.attr("data-original")
            if (path.isEmpty()) {
                path = item.attr("src")
            }
            if (path.isNotEmpty() && !SKIP_DETAIL.contains(path)) {
                list.add(DetailView.createImage(path))
            }
        }
        return list
    }

    override fun getDetailShare(episode: Episode, detail: Detail) = ShareItem().apply {
        title = episode.title + " / " + detail.title
        url = String.format(SHARE_URL, detail.webtoonId, detail.episodeId)
    }

    override val method: String
        get() = NetworkSupportApi.GET

    override val url: String
        get() = String.format(SHARE_URL, webToonId, episodeId)
}
