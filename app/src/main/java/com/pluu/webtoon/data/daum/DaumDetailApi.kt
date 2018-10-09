package com.pluu.webtoon.data.daum

import com.pluu.kotlin.iterator
import com.pluu.webtoon.data.DetailRequest
import com.pluu.webtoon.data.IRequest
import com.pluu.webtoon.data.REQUEST_METHOD
import com.pluu.webtoon.data.impl.AbstractDetailApi
import com.pluu.webtoon.di.NetworkUseCase
import com.pluu.webtoon.item.*
import com.pluu.webtoon.utils.safeAPi
import org.json.JSONObject

/**
 * 다음 웹툰 상세 API
 * Created by pluu on 2017-04-22.
 */
class DaumDetailApi(
    networkUseCase: NetworkUseCase
) : AbstractDetailApi(networkUseCase) {
    override fun parseDetail(param: DetailRequest): DetailResult {
        val id = param.episodeId

        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val apiResult = safeAPi(requestApi(createApi(id))) { response ->
            JSONObject(response)
        }

        val responseData = when (apiResult) {
            is Result.Success -> apiResult.data
            is Result.Error -> return DetailResult.ErrorResult(ERROR_TYPE.DEFAULT_ERROR)
        }

        ///////////////////////////////////////////////////////////////////////////
        // Parse Data
        ///////////////////////////////////////////////////////////////////////////

        val data =
            responseData.optJSONObject("data") ?: return DetailResult.ErrorResult(ERROR_TYPE.NOT_SUPPORT)

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
        json.optJSONArray("webtoonImages")?.iterator()?.forEach { it ->
            list.add(DetailView(it.optString("url")))
        }
        json.optJSONArray("webtoonEpisodePages")?.iterator()?.forEach { it ->
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

    override fun getDetailShare(episode: EpisodeInfo, detail: DetailResult.Detail) = ShareItem(
        title = "${episode.title} / ${detail.title}",
        url = "$SHARE_URL$detail.episodeId"
    )

    private fun createApi(id: String): IRequest = IRequest(
        method = REQUEST_METHOD.POST,
        url = DETAIL_URL,
        params = hashMapOf("id" to id)
    )

    companion object {
        private const val DETAIL_URL = "http://m.webtoon.daum.net/data/mobile/webtoon/viewer"
        private const val SHARE_URL = "http://m.webtoon.daum.net/m/webtoon/viewer/"
    }
}
