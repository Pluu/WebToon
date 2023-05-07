package com.pluu.webtoon.data.remote.api.site.kakao

import com.pluu.utils.asSequence
import com.pluu.webtoon.data.remote.R
import com.pluu.webtoon.data.remote.api.EpisodeApi
import com.pluu.webtoon.data.remote.model.IRequest
import com.pluu.webtoon.data.remote.model.REQUEST_METHOD
import com.pluu.webtoon.data.remote.network.INetworkUseCase
import com.pluu.webtoon.data.remote.utils.ResourceLoader
import com.pluu.webtoon.data.remote.utils.mapJson
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.model.EpisodeResult
import com.pluu.webtoon.model.Result
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.json.JSONObject
import javax.inject.Inject

/**
 * 카카오 페이지 웹툰 EpisodeInfo API
 * Created by pluu on 2017-04-25.
 */
internal class KakaoEpisodeApi @Inject constructor(
    private val networkUseCase: INetworkUseCase,
    private val resourceLoader: ResourceLoader
) : EpisodeApi, INetworkUseCase by networkUseCase {

    override suspend fun invoke(param: EpisodeApi.Param): Result<EpisodeResult> {
        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val responseData = requestApi(
            createApi(id = param.toonId, page = param.page)
        ).mapJson()
            .let { result ->
                when (result) {
                    is Result.Success -> result.data
                    is Result.Error -> return Result.Error(result.throwable)
                }
            }

        ///////////////////////////////////////////////////////////////////////////
        // Parse Data
        ///////////////////////////////////////////////////////////////////////////

        val content = responseData.optJSONObject("data")
            ?.optJSONObject("contentHomeProductList")
            ?: return Result.Success(EpisodeResult(emptyList()))

        val list = content.getJSONArray("edges")
            .asSequence()
            .map { json ->
                parseList(param.toonId, json.getJSONObject("node"))
            }.toList()

        val episodePage = EpisodeResult(list)
        episodePage.nextLink = content.optJSONObject("pageInfo")
            ?.optBoolean("hasNextPage", false)
            ?.takeIf { hasNextPage -> hasNextPage }
            ?.toString()

        if (param.page == 0) {
            episodePage.first = parseFirstKey(param.toonId)
                ?.let { firstId ->
                    list.firstOrNull()?.copy(id = firstId)
                }
        }

        return Result.Success(episodePage)
    }

    private fun parseList(toonId: String, json: JSONObject): EpisodeInfo {
        val single = json.getJSONObject("single")
        return EpisodeInfo(
            id = single.getString("productId"),
            toonId = toonId,
            title = single.getString("title"),
            image = "https:${single.getString("thumbnail")}",
            updateDate = json.getJSONArray("row2").getString(0),
            isLoginNeed = single.optBoolean("isFree", false).not()
        )
    }

    private suspend fun parseFirstKey(toonId: String): String? {
        // TODO: 첫화보기
        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////
        return null
    }

    private fun createApi(id: String, page: Int): IRequest =
        IRequest(
            method = REQUEST_METHOD.POST,
            url = "https://page.kakao.com/graphql",
            params = generateApiParams(
                seriesId = id,
                pageNo = page
            )
        )

    private fun generateApiParams(
        seriesId: String,
        pageNo: Int
    ): Map<String, String> {
        val query = resourceLoader.readRawResource(R.raw.kakao_episode)
        val variables = buildJsonObject {
            put("seriesId", seriesId)
            put("sortType", "desc")
            if (pageNo > 0) {
                put("after", (pageNo * 25).toString())
                put("boughtOnly", false)
            }
        }.toString()

        return mapOf(
            "query" to query,
            "variables" to variables
        )
    }
}
