package com.pluu.webtoon.support.nate

import com.pluu.webtoon.data.model.IRequest
import com.pluu.webtoon.model.Result
import com.pluu.webtoon.data.network.INetworkUseCase
import com.pluu.webtoon.domain.moel.DetailResult
import com.pluu.webtoon.domain.moel.DetailView
import com.pluu.webtoon.domain.moel.ERROR_TYPE
import com.pluu.webtoon.domain.usecase.DetailUseCase
import com.pluu.webtoon.domain.usecase.param.DetailRequest
import com.pluu.webtoon.network.mapDocument
import org.jsoup.nodes.Document

/**
 * 네이트 웹툰 상세 API
 * Created by pluu on 2017-04-27.
 */
internal class NateDetailApi(
    private val networkUseCase: INetworkUseCase
) : DetailUseCase, INetworkUseCase by networkUseCase {

    private lateinit var webToonId: String
    private lateinit var episodeId: String

    override suspend fun invoke(param: DetailRequest): DetailResult {
        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val responseData: Document = requestApi(createApi(param.toonId, param.episodeId))
            .mapDocument()
            .let { result ->
                when (result) {
                    is Result.Success -> result.data
                    is Result.Error -> return DetailResult.ErrorResult(ERROR_TYPE.DEFAULT_ERROR(result.exception))
                }
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
            responseData.select(".toonView img").mapTo(list) {
                DetailView(
                    it.attr("src")
                )
            }
            this.list = list
        }
        return ret
    }

    private fun createApi(toonId: String, episodeId: String): IRequest =
        IRequest(
            url = detailCreate(
                toonId,
                episodeId
            )
        )

    companion object {
        val detailCreate = { toonId: String, episodeId: String ->
            "http://m.comics.nate.com/main2/webtoon/WebtoonView.php?btno=$toonId&bsno=$episodeId"
        }
    }
}
