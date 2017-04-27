package com.pluu.support.tstore

import android.content.Context
import com.pluu.support.impl.AbstractDetailApi
import com.pluu.support.impl.NetworkSupportApi
import com.pluu.webtoon.item.Detail
import com.pluu.webtoon.item.DetailView
import com.pluu.webtoon.item.Episode
import com.pluu.webtoon.item.ShareItem
import org.jsoup.Jsoup

/**
 * TStore 웹툰 상세 API
 * Created by pluu on 2017-04-27.
 */
class TStoreDetailApi(context: Context) : AbstractDetailApi(context) {

    private lateinit var id: String

    override fun parseDetail(episode: Episode): Detail {
        // TODO : 디테일 정보 파싱 수정
        this.id = episode.episodeId

        val ret = Detail().apply {
            webtoonId = episode.toonId
            episodeId = id
        }

        val doc = try {
            Jsoup.parse(requestApi())
        } catch (e: Exception) {
            e.printStackTrace()
            ret.list = emptyList<DetailView>()
            return ret
        }

        ret.title = doc.select(".episode-num").text()

        ret.prevLink = EPISODE_ID.find(doc.select("a[class=btn-episode-link btn-episode-prev]")?.
                attr("href") ?: "")?.value
        ret.nextLink = EPISODE_ID.find(doc.select("a[class=btn-episode-link btn-episode-next]")?.
                attr("href") ?: "")?.value

        val list = mutableListOf<DetailView>()
        val urlPattern = "(?<=FILE_POS = \\\").+(?=\\\";)".toRegex()
        val endPattern = "(?<=bookPages = Number\\(\\\")\\d+(?=\\\"\\))".toRegex()

        doc.select("script[type=text/javascript]").forEach { script ->
            with(script.html()) {
                if (!contains("bookPages")) {
                    return@forEach
                }
                with(this.replace("\r\n", "")) {
                    val urlMatcher = urlPattern.find(this)
                    val endMatcher = endPattern.find(this)
                    if (urlMatcher == null || endMatcher == null) {
                        return@forEach
                    }
                    val imgUrl = urlMatcher.value
                    (1..endMatcher.value.toInt())
                            .mapTo(list) {
                                DetailView.createImage("$MAIN_HOST_URL$imgUrl/$it.jpg")
                            }
                }
            }
        }
        ret.list = list
        return ret
    }

    override fun getDetailShare(episode: Episode, detail: Detail) = ShareItem().apply {
        title = "${episode.title} / ${detail.title}"
        url = "http://m.tstore.co.kr/mobilepoc/webtoon/webtoonDetail.omp?prodId=${detail.episodeId}&PrePageNm=/detail/webtoon/mw"
    }

    override val method: String
        get() = NetworkSupportApi.GET

    override val url: String
        get() = "https://www.myktoon.com/api/work/getTimesDetailImageList.kt"

    companion object {
        private val MAIN_HOST_URL = "http://m.tstore.co.kr"
        private val EPISODE_ID = "(?<=sub_product_id\':\').+(?=\'\\})".toRegex()
    }
}
