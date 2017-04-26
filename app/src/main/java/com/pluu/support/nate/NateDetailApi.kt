package com.pluu.support.nate

import android.content.Context
import com.pluu.support.impl.AbstractDetailApi
import com.pluu.support.impl.NetworkSupportApi
import com.pluu.webtoon.item.Detail
import com.pluu.webtoon.item.DetailView
import com.pluu.webtoon.item.Episode
import com.pluu.webtoon.item.ShareItem
import org.jsoup.Jsoup
import java.util.*

/**
 * 네이트 웹툰 상세 API
 * Created by pluu on 2017-04-27.
 */
class NateDetailApi(context: Context) : AbstractDetailApi(context) {

    private val DETAIL_URL = "http://m.comics.nate.com/main2/webtoon/WebtoonView.php?btno=%s&bsno=%s"
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
            ret.list = ArrayList<DetailView>()
            return ret
        }
        ret.apply {
            title = doc.select(".tvi_header").text()

            doc.select(".btn_prev")?.apply {
                prevLink = if (isNotEmpty()) "/view/" + first().attr("href") else null
            }
            doc.select(".btn_next")?.apply {
                nextLink = if (isNotEmpty()) "/view/" + first().attr("href") else null
            }
            val list = mutableListOf<DetailView>()
            doc.select(".toonView img").mapTo(list) { DetailView.createImage(it.attr("src")) }
            this.list = list
        }
        return ret
    }

    override fun getDetailShare(episode: Episode, detail: Detail) = ShareItem().apply {
        title = "${episode.title} / ${detail.title}"
        url = DETAIL_URL.format(episode.toonId, episode.episodeId)
    }

    override val method: String
        get() = NetworkSupportApi.GET

    override val url: String
        get() = DETAIL_URL.format(webToonId, episodeId)

}
