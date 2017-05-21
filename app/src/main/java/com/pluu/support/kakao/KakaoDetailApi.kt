package com.pluu.support.kakao

import android.content.Context
import com.pluu.support.impl.AbstractDetailApi
import com.pluu.support.impl.NetworkSupportApi
import com.pluu.webtoon.item.Detail
import com.pluu.webtoon.item.DetailView
import com.pluu.webtoon.item.Episode
import com.pluu.webtoon.item.ShareItem
import org.jsoup.Jsoup

/**
 * 카카오 페이지 웹툰 상세 API
 * Created by pluu on 2017-04-25.
 */
class KakaoDetailApi(context: Context) : AbstractDetailApi(context) {

    private val DETAIL_URL = "http://page.kakao.com/viewer?productId=%s&categoryUid=10&subCategoryUid=0"
    private lateinit var id: String

    override fun parseDetail(episode: Episode): Detail {
        this.id = episode.episodeId

        val ret = Detail().apply {
            webtoonId = episode.toonId
        }

        val doc = try {
            Jsoup.parse(requestApi())
        } catch (e: Exception) {
            e.printStackTrace()
            ret.list = emptyList<DetailView>()
            return ret
        }

        ret.apply {
            title = doc.select("span[class=title ellipsis]").text()
            episodeId = id

            prevLink = doc.select(".prevSectionBtn").attr("data-productid").takeIf {
                "0" != it
            }
            nextLink = doc.select(".nextSectionBtn").attr("data-productid").takeIf {
                "0" != it
            }

            val list = mutableListOf<DetailView>()
            mapOf(".targetImg" to "data-original",
                    ".viewWrp li input" to "value",
                    ".clickViewerWrp li input[class=originSrc]" to "value")
                    .forEach { (target, attrs) ->
                        doc.select(target).mapTo(list) { DetailView.createImage(it.attr(attrs)) }
                    }

            this.list = list
        }
        return ret
    }

    override fun getDetailShare(episode: Episode, detail: Detail) = ShareItem(
            title = "${episode.title} / ${detail.title}",
            url = DETAIL_URL.format(episode.episodeId)
    )

    override val method: String
        get() = NetworkSupportApi.GET

    override val url: String
        get() = DETAIL_URL.format(id)
}
