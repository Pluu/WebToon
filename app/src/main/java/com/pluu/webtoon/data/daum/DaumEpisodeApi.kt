package com.pluu.webtoon.data.daum

import com.pluu.kotlin.asSequence
import com.pluu.webtoon.data.EpisodeRequest
import com.pluu.webtoon.data.IRequest
import com.pluu.webtoon.data.REQUEST_METHOD
import com.pluu.webtoon.data.impl.AbstractEpisodeApi
import com.pluu.webtoon.di.INetworkUseCase
import com.pluu.webtoon.item.EpisodeInfo
import com.pluu.webtoon.item.EpisodeResult
import com.pluu.webtoon.item.Result
import com.pluu.webtoon.utils.mapJson
import com.pluu.webtoon.utils.safeAPi
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
            val page = responseData.optJSONObject("page")

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
                    image = it.optJSONObject("thumbnailImage").optString("url"),
                    updateDate = it.optString("dateCreated"),
                    rate = it.optJSONObject("voteTarget").optString("voteTotalScore"),
                    isLoginNeed = it.optInt("price", 0) > 0
                )
            }.toList()

    private fun parsePage(obj: JSONObject): Boolean {
        val total = obj.optInt("size", 1)
        val current = obj.optInt("no", 1)
        return total >= current + 1
    }

    private fun createApi(param: EpisodeRequest): IRequest = IRequest(
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
