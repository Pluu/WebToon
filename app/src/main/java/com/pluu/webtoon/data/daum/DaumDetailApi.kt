package com.pluu.webtoon.data.daum

import com.pluu.kotlin.iterator
import com.pluu.webtoon.data.DetailRequest
import com.pluu.webtoon.data.IRequest
import com.pluu.webtoon.data.REQUEST_METHOD
import com.pluu.webtoon.data.impl.AbstractDetailApi
import com.pluu.webtoon.di.INetworkUseCase
import com.pluu.webtoon.item.DetailResult
import com.pluu.webtoon.item.DetailView
import com.pluu.webtoon.item.ERROR_TYPE
import com.pluu.webtoon.item.Result
import com.pluu.webtoon.utils.mapJson
import org.json.JSONObject

/**
 * 다음 웹툰 상세 API
 * Created by pluu on 2017-04-22.
 */
class DaumDetailApi(
    private val networkUseCase: INetworkUseCase
) : AbstractDetailApi, INetworkUseCase by networkUseCase {

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
                    is Result.Error -> return DetailResult.ErrorResult(ERROR_TYPE.DEFAULT_ERROR)
                }
            }

        ///////////////////////////////////////////////////////////////////////////
        // Parse Data
        ///////////////////////////////////////////////////////////////////////////

        val data = responseData.optJSONObject("data")
            ?: return DetailResult.ErrorResult(ERROR_TYPE.NOT_SUPPORT)

        if (data.optJSONObject("webtoonEpisode")?.optInt("price", 0) ?: 0 > 0) {
            return DetailResult.ErrorResult(ERROR_TYPE.COIN_NEED)
        }

        val titleInfo = data.optJSONObject("webtoonEpisode")

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
            ?.iterator()
            ?.forEach {
                list.add(
                    DetailView(
                        it.optJSONArray("webtoonEpisodePageMultimedias")
                            .optJSONObject(0)
                            .optJSONObject("image")
                            .optString("url")
                    )
                )
            }
        return list
    }

    private fun createApi(id: String): IRequest = IRequest(
        method = REQUEST_METHOD.POST,
        url = "http://m.webtoon.daum.net/data/mobile/webtoon/viewer",
        params = hashMapOf("id" to id)
    )
}
