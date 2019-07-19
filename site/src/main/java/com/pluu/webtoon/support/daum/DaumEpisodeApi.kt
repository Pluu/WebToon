package com.pluu.webtoon.support.daum

import com.pluu.core.Result
import com.pluu.core.asSequence
import com.pluu.core.orEmpty
import com.pluu.webtoon.data.model.IRequest
import com.pluu.webtoon.data.model.REQUEST_METHOD
import com.pluu.webtoon.data.network.INetworkUseCase
import com.pluu.webtoon.data.network.mapJson
import com.pluu.webtoon.data.network.safeAPi
import com.pluu.webtoon.domain.base.AbstractEpisodeApi
import com.pluu.webtoon.domain.moel.EpisodeInfo
import com.pluu.webtoon.domain.moel.EpisodeResult
import com.pluu.webtoon.domain.usecase.param.EpisodeRequest
import org.json.JSONArray
import org.json.JSONObject

/**
 * 다음 웹툰 EpisodeInfo API
 * Created by pluu on 2017-04-21.
 */
class DaumEpisodeApi(
    private val networkUseCase: INetworkUseCase
) : AbstractEpisodeApi, INetworkUseCase by networkUseCase {

    override suspend fun invoke(param: EpisodeRequest): Result<EpisodeResult> {

        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val responseData = requestApi(createApi(param))
            .mapJson()
            .let { result ->
                when (result) {
                    is Result.Success -> result.data
                    is Result.Error -> return Result.Error(result.exception)
                }
            }

        ///////////////////////////////////////////////////////////////////////////
        // Parse Data
        ///////////////////////////////////////////////////////////////////////////

        val data: JSONArray? = responseData
            .optJSONObject("data")
            ?.optJSONArray("webtoonEpisodes")
        val episodes = if (data != null) {
            parseList(param.toonId, data)
        } else {
            emptyList()
        }

        val result = EpisodeResult(episodes)

        if (episodes.isNotEmpty()) {
            val nick = param.toonId
            val page = responseData.optJSONObject("page").orEmpty()

            result.nextLink = nick.takeIf { parsePage(page) }
            result.first = getFirstEpisode(nick)
                ?.optJSONObject("data")
                ?.optString("firstEpisodeId")
                ?.let { firstId ->
                    episodes.first().copy(id = firstId)
                }
        }
        return Result.Success(result)
    }

    private suspend fun getFirstEpisode(nick: String): JSONObject? {
        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val apiResult = safeAPi(
            requestApi(
                IRequest(url = PREFIX_FIRST_URL + nick)
            )
        ) { response ->
            JSONObject(response)
        }

        return if (apiResult is Result.Success) {
            apiResult.data
        } else {
            null
        }
    }

    private fun parseList(toonId: String, data: JSONArray): List<EpisodeInfo> =
        data.asSequence()
            .map {
                EpisodeInfo(
                    id = it.optString("id"),
                    toonId = toonId,
                    title = it.optString("title"),
                    image = it.optJSONObject("thumbnailImage")?.optString("url").orEmpty(),
                    updateDate = it.optString("dateCreated"),
                    rate = it.optJSONObject("voteTarget")?.optString("voteTotalScore").orEmpty(),
                    isLoginNeed = it.optInt("price", 0) > 0
                )
            }.toList()

    private fun parsePage(obj: JSONObject): Boolean {
        val total = obj.optInt("size", 1)
        val current = obj.optInt("no", 1)
        return total >= current + 1
    }

    private fun createApi(param: EpisodeRequest): IRequest =
        IRequest(
            method = REQUEST_METHOD.POST,
            url = "http://m.webtoon.daum.net/data/mobile/webtoon/list_episode_by_nickname",
            params = mutableMapOf<String, String>().apply {
                put("page_size", "20")
                put("nickname", param.toonId)
                put("page_no", (param.page + 1).toString())
            }
        )

    companion object {
        private const val PREFIX_FIRST_URL =
            "http://m.webtoon.daum.net/data/mobile/webtoon/view?nickname="
    }
}
