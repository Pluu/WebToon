package com.pluu.webtoon.data.onestore

import com.pluu.kotlin.asSequence
import com.pluu.webtoon.data.DetailRequest
import com.pluu.webtoon.data.IRequest
import com.pluu.webtoon.data.REQUEST_METHOD
import com.pluu.webtoon.data.impl.AbstractDetailApi
import com.pluu.webtoon.di.INetworkUseCase
import com.pluu.webtoon.item.DetailResult
import com.pluu.webtoon.item.DetailView
import com.pluu.webtoon.item.ERROR_TYPE
import com.pluu.webtoon.item.Result
import com.pluu.webtoon.utils.safeAPi
import org.json.JSONArray
import org.jsoup.Jsoup

/**
 * TStore 웹툰 상세 API
 * Created by pluu on 2017-04-27.
 */
class OneStoreDetailApi(
    private val networkUseCase: INetworkUseCase
) : AbstractDetailApi, INetworkUseCase by networkUseCase {

    override fun invoke(param: DetailRequest): DetailResult {
        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val mainRequest = safeAPi(
            requestApi(createApi(param.episodeId))
        ) { response ->
            Jsoup.parse(response)
        }

        val mainResponseData = when (mainRequest) {
            is Result.Success -> mainRequest.data
            is Result.Error -> return DetailResult.ErrorResult(ERROR_TYPE.DEFAULT_ERROR)
        }

        val timesseq = mainResponseData
            .select("#ifrmResizing")
            .attr("src")?.let {
                CURRENT_ID.find(it)?.value
            } ?: return DetailResult.ErrorResult(ERROR_TYPE.NOT_SUPPORT)


        // Image List
        val imageRequest = IRequest(
            method = REQUEST_METHOD.POST,
            url = "https://v2.myktoon.com/mw/open/onestore/getTimesDetailImageList.kt",
            params = mapOf("timesseq" to timesseq)
        )

        val imageResult = safeAPi(requestApi(imageRequest)) { response ->
            JSONArray(response)
        }

        val imageResponseData = when (imageResult) {
            is Result.Success -> imageResult.data
            is Result.Error -> return DetailResult.ErrorResult(ERROR_TYPE.NOT_SUPPORT)
        }

        ///////////////////////////////////////////////////////////////////////////
        // Parse Data
        ///////////////////////////////////////////////////////////////////////////

        val ret = DetailResult.Detail(
            webtoonId = param.toonId,
            episodeId = param.episodeId
        )

        ret.title = mainResponseData.select(".detail-episode-header-ti").text()
        ret.prevLink = mainResponseData
            .select("[class=btn-detail-episode-btn btn-detail-episode-prev]")
            .attr("href")?.let {
                EPISODE_ID.find(it)?.value
            }
        ret.nextLink = mainResponseData
            .select("[class=btn-detail-episode-btn btn-detail-episode-next]")
            .attr("href")?.let {
                EPISODE_ID.find(it)?.value
            }

        ret.list = imageResponseData.asSequence()
            .map {
                DetailView(it.optString("imagepath"))
            }
            .toList()
        return ret
    }

    private fun createApi(episodeId: String): IRequest = IRequest(
        url = "http://m.onestore.co.kr/mobilepoc/webtoon/webtoonDetail.omp?prodId=$episodeId&PrePageNm="
    )

    companion object {
        private val EPISODE_ID = "(?<=')[A-Za-z0-9]+".toRegex()
        private val CURRENT_ID = "(?<=timesseq=)\\d+".toRegex()
    }
}
