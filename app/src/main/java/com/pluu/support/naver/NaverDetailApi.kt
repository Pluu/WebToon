package com.pluu.support.naver

import android.content.Context
import com.pluu.support.impl.AbstractDetailApi
import com.pluu.support.impl.REQUEST_METHOD
import com.pluu.webtoon.item.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * 네이버 웹툰 상세 API
 * Created by pluu on 2017-04-20.
 */
class NaverDetailApi(context: Context) : AbstractDetailApi(context) {

    private val SKIP_DETAIL = arrayOf(
        "http://static.naver.com/m/comic/im/txt_ads.png",
        "http://static.naver.com/m/comic/im/toon_app_pop.png"
    )

    private lateinit var webToonId: String
    private lateinit var episodeId: String

    override fun parseDetail(episode: Episode): Detail {
        this.webToonId = episode.toonId
        this.episodeId = episode.episodeId

        val ret = Detail().apply {
            webtoonId = episode.toonId
            episodeId = episode.episodeId
        }

        val doc = try {
            Jsoup.parse(requestApi())
        } catch (e: Exception) {
            e.printStackTrace()
            ret.list = emptyList()
            return ret
        }

        ret.title = doc.select("div[class=chh] span, h1[class=tit]").first()?.text()

        if (doc.select("div[class=viewer cuttoon]")?.isNotEmpty() == true) {
            // 컷툰
            parseCutToon(ret, doc)
            return ret
        }
        if (doc.select(".oz-loader")?.isNotEmpty() == true) {
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
        doc.select(".paging_wrap").apply {
            select("[data-type=next]").takeIf { it.isNotEmpty() }.let {
                ret.nextLink = (episodeId.toInt() + 1).toString()
            }
            select("[data-type=prev]").takeIf { it.isNotEmpty() }.let {
                ret.prevLink = (episodeId.toInt() - 1).toString()
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
            .map { url -> DetailView(url) }
    }

    private fun parseDetailNormalType(doc: Document) =
        doc.select("#toonLayer li img")
            .map {
                it.attr("data-original").takeIf {
                    it.isNotEmpty()
                } ?: it.attr("src")
            }
            .filter { it.isNotEmpty() && !SKIP_DETAIL.contains(it) }
            .map { DetailView(it) }

    private fun getShareUrl(webtoonId: String, episodeId: String) =
        "http://m.comic.naver.com/webtoon/detail.nhn?titleId=$webtoonId&no=$episodeId"

    override fun getDetailShare(episode: Episode, detail: Detail) = ShareItem(
        title = "${episode.title} / ${detail.title}",
        url = getShareUrl(detail.webtoonId.orEmpty(), detail.episodeId.orEmpty())
    )

    override val method: REQUEST_METHOD = REQUEST_METHOD.GET

    override val url: String
        get() = getShareUrl(webToonId, episodeId)
}
