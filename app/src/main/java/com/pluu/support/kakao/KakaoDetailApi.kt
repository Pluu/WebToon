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

        val ret = Detail()
        ret.webtoonId = episode.toonId

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
            doc.select(".targetImg").mapTo(list) { DetailView.createImage(it.attr("data-original")) }
            doc.select(".viewWrp li input").mapTo(list) { DetailView.createImage(it.attr("value")) }

            this.list = list
        }
        return ret
    }

    override fun getDetailShare(episode: Episode, detail: Detail): ShareItem {
        val item = ShareItem()
        item.title = "${episode.title} / ${detail.title}"
        item.url = DETAIL_URL.format(episode.episodeId)
        return item
    }

    override val method: String
        get() = NetworkSupportApi.GET

    override val url: String
        get() = DETAIL_URL.format(id)
}
