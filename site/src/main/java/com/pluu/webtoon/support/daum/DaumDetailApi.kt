package com.pluu.webtoon.support.daum

import com.pluu.core.asSequence
import com.pluu.core.iterator
import com.pluu.webtoon.data.model.IRequest
import com.pluu.webtoon.data.model.REQUEST_METHOD
import com.pluu.webtoon.data.network.INetworkUseCase
import com.pluu.webtoon.data.network.mapJson
import com.pluu.webtoon.domain.model.DetailResult
import com.pluu.webtoon.domain.model.DetailView
import com.pluu.webtoon.domain.model.ERROR_TYPE
import com.pluu.webtoon.domain.usecase.DetailUseCase
import com.pluu.webtoon.domain.usecase.param.DetailRequest
import com.pluu.webtoon.model.Result
import org.json.JSONObject

/**
 * 다음 웹툰 상세 API
 * Created by pluu on 2017-04-22.
 */
internal class DaumDetailApi(
    private val networkUseCase: INetworkUseCase
) : DetailUseCase, INetworkUseCase by networkUseCase {

    override suspend fun invoke(param: DetailRequest): DetailResult {
        val id = param.episodeId

        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val responseData = requestApi(createApi(id))
            .mapJson()
            .let { result ->
                when (result) {
                    is Result.Success -> result.data
                    is Result.Error -> return DetailResult.ErrorResult(ERROR_TYPE.DEFAULT_ERROR(result.exception))
                }
            }

        ///////////////////////////////////////////////////////////////////////////
        // Parse Data
        ///////////////////////////////////////////////////////////////////////////

        val data = responseData.optJSONObject("data")
            ?: return DetailResult.ErrorResult(ERROR_TYPE.NOT_SUPPORT)

        val titleInfo = data.optJSONObject("webtoonEpisode")
            ?: return DetailResult.ErrorResult(ERROR_TYPE.COIN_NEED)

        val ret = DetailResult.Detail(
            webtoonId = param.toonId,
            episodeId = titleInfo.optString("id")
        )
        ret.title = titleInfo.optString("title")

        ret.nextLink = data.optInt("nextEpisodeId", 0).takeIf {
            it > 0
        }?.toString()
        ret.prevLink = data.optInt("prevEpisodeId", 0).takeIf {
            it > 0
        }?.toString()

        ret.list = defaultDetailParse(data)

        return ret
    }

    private fun defaultDetailParse(json: JSONObject): List<DetailView> {
        val list = mutableListOf<DetailView>()
        json.optJSONArray("webtoonImages")
            ?.iterator()
            ?.forEach {
                list.add(DetailView(it.optString("url")))
            }
        json.optJSONArray("webtoonEpisodePages")
            ?.asSequence()
            ?.mapNotNull {
                it.optJSONArray("webtoonEpisodePageMultimedias")
                    ?.optJSONObject(0)
                    ?.optJSONObject("image")
                    ?.optString("url")
            }
            ?.forEach { url ->
                list.add(DetailView(url))
            }
        return list
    }

    private fun createApi(id: String): IRequest = IRequest(
        method = REQUEST_METHOD.POST,
        url = "http://m.webtoon.daum.net/data/mobile/webtoon/viewer",
        params = hashMapOf("id" to id)
    )
}
