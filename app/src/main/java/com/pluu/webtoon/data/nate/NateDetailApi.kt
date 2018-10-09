package com.pluu.webtoon.data.nate

import com.pluu.webtoon.data.DetailRequest
import com.pluu.webtoon.data.IRequest
import com.pluu.webtoon.data.impl.AbstractDetailApi
import com.pluu.webtoon.di.NetworkUseCase
import com.pluu.webtoon.item.*
import com.pluu.webtoon.utils.safeAPi
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

    override fun parseDetail(param: DetailRequest): DetailResult {
        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val apiResult = safeAPi(
            requestApi(
                createApi(
                    id = param.toonId,
                    episodeId = param.episodeId
                )
            )
        ) { response ->
            Jsoup.parse(response)
        }

        val responseData = when (apiResult) {
            is Result.Success -> apiResult.data
            is Result.Error -> return DetailResult.ErrorResult(ERROR_TYPE.DEFAULT_ERROR)
        }

        ///////////////////////////////////////////////////////////////////////////
        // Parse Data
        ///////////////////////////////////////////////////////////////////////////

        this.webToonId = param.toonId
        this.episodeId = param.episodeId

        val ret = DetailResult.Detail(
            webtoonId = param.toonId,
            episodeId = param.episodeId
        )

        ret.apply {
            title = responseData.select(".tvi_header").text()

            responseData.select(".btn_prev")?.apply {
                prevLink = if (isNotEmpty()) "/view/" + first().attr("href") else null
            }
            responseData.select(".btn_next")?.apply {
                nextLink = if (isNotEmpty()) "/view/" + first().attr("href") else null
            }
            val list = mutableListOf<DetailView>()
            responseData.select(".toonView img").mapTo(list) { DetailView(it.attr("src")) }
            this.list = list
        }
        return ret
    }

    override fun getDetailShare(episode: EpisodeInfo, detail: DetailResult.Detail) = ShareItem(
        title = "${episode.title} / ${detail.title}",
        url = DETAIL_URL.format(detail.webtoonId, detail.episodeId)
    )

    private fun createApi(id: String, episodeId: String): IRequest = IRequest(
        url = DETAIL_URL.format(id, episodeId)
    )
}
