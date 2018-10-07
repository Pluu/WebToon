package com.pluu.support.nate

import com.pluu.support.impl.AbstractDetailApi
import com.pluu.support.impl.REQUEST_METHOD
import com.pluu.webtoon.di.NetworkUseCase
import com.pluu.webtoon.item.*
import org.jsoup.Jsoup

/**
 * 네이트 웹툰 상세 API
 * Created by pluu on 2017-04-27.
 */
class NateDetailApi(
    networkUseCase: NetworkUseCase
) : AbstractDetailApi(networkUseCase) {

    private val DETAIL_URL =
        "http://m.comics.nate.com/main2/webtoon/WebtoonView.php?btno=%s&bsno=%s"
    private lateinit var webToonId: String
    private lateinit var episodeId: String

    override fun parseDetail(episode: IEpisode): DetailResult {
        this.webToonId = episode.webToonId
        this.episodeId = episode.episodeId

        val ret = DetailResult.Detail(
            webtoonId = episode.webToonId,
            episodeId = episode.episodeId
        )

        val doc = try {
            Jsoup.parse(requestApi())
        } catch (e: Exception) {
            e.printStackTrace()
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
            doc.select(".toonView img").mapTo(list) { DetailView(it.attr("src")) }
            this.list = list
        }
        return ret
    }

    override fun getDetailShare(episode: Episode, detail: DetailResult.Detail) = ShareItem(
        title = "${episode.title} / ${detail.title}",
        url = DETAIL_URL.format(episode.toonId, episode.episodeId)
    )

    override val method: REQUEST_METHOD = REQUEST_METHOD.GET

    override val url: String
        get() = DETAIL_URL.format(webToonId, episodeId)
}
