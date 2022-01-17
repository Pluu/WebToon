package com.pluu.webtoon.data.remote.api.site.daum

import com.pluu.utils.asSequence
import com.pluu.webtoon.data.remote.api.EpisodeApi
import com.pluu.webtoon.data.remote.model.IRequest
import com.pluu.webtoon.data.remote.model.REQUEST_METHOD
import com.pluu.webtoon.data.remote.network.INetworkUseCase
import com.pluu.webtoon.data.remote.utils.mapJson
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.model.EpisodeResult
import com.pluu.webtoon.model.LandingInfo
import com.pluu.webtoon.model.Result
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

/**
 * 카카오 웹툰 EpisodeInfo API
 * Created by pluu on 2017-04-21.
 */
internal class DaumEpisodeApi @Inject constructor(
    private val networkUseCase: INetworkUseCase
) : EpisodeApi, INetworkUseCase by networkUseCase {

    override suspend fun invoke(param: EpisodeApi.Param): Result<EpisodeResult> {

        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val responseData = requestApi(createApi(param))
            .mapJson()
            .let { result ->
                when (result) {
                    is Result.Success -> result.data
                    is Result.Error -> return Result.Error(result.throwable)
                }
            }

        ///////////////////////////////////////////////////////////////////////////
        // Parse Data
        ///////////////////////////////////////////////////////////////////////////

        val data: JSONArray? = responseData
            .optJSONObject("data")
            ?.optJSONArray("episodes")
        val episodes = if (data != null) {
            parseList(param.toonId, data)
        } else {
            emptyList()
        }

        val result = EpisodeResult(episodes)

        if (episodes.isNotEmpty()) {
            result.nextLink = parseNextPage(responseData, param)

//            result.first = responseData
//                .getJSONObject("data")
//                .getJSONObject("first")
//                ?.let { firstId ->
//                    episodes.first().copy(id = firstId)
//                }
        }
        return Result.Success(result)
    }

    private fun parseNextPage(
        responseData: JSONObject,
        param: EpisodeApi.Param
    ): String? = responseData.getJSONObject("meta")
        .getJSONObject("pagination")
        .optBoolean("last")
        .takeUnless { it }
        ?.let {
            (param.page + 1).toString()
        }

    private fun parseList(toonId: String, data: JSONArray): List<EpisodeInfo> =
        data.asSequence()
            .map {
                val id = it.optString("id")
                val seoId = it.getString("seoId")
                EpisodeInfo(
                    id = id,
                    toonId = toonId,
                    title = it.optString("title"),
                    image = it.getJSONObject("asset").getString("thumbnailImage") + ".webp",
                    updateDate = it.optString("serialStartDateTime"),
                    isLoginNeed = when {
                        it.optBoolean("adult") -> true
                        else -> it.getString("useType") != "FREE"
                    },
                    landingInfo = LandingInfo.Browser(
                        "https://webtoon.kakao.com/viewer/${seoId}/${id}"
                    )
                )
            }.toList()

    private fun createApi(param: EpisodeApi.Param): IRequest =
        IRequest(
            method = REQUEST_METHOD.GET,
            url = "https://gateway-kw.kakao.com/episode/v1/views/content-home/contents/${param.toonId}/episodes",
            params = mutableMapOf<String, String>().apply {
                put("sort", "-NO")
                put("offset", "${param.page * 30}")
                put("limit", "30")
            },
            headers = mapOf(
                "Accept-Language" to "ko"
            )
        )
}
