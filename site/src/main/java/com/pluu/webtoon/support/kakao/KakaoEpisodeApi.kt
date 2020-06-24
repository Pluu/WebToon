package com.pluu.webtoon.support.kakao

import com.pluu.core.asSequence
import com.pluu.webtoon.data.model.IRequest
import com.pluu.webtoon.data.model.REQUEST_METHOD
import com.pluu.webtoon.data.network.INetworkUseCase
import com.pluu.webtoon.data.network.mapJson
import com.pluu.webtoon.domain.model.EpisodeInfo
import com.pluu.webtoon.domain.model.EpisodeResult
import com.pluu.webtoon.domain.usecase.EpisodeUseCase
import com.pluu.webtoon.domain.usecase.param.EpisodeRequest
import com.pluu.webtoon.model.Result
import org.json.JSONArray

/**
 * 카카오 페이지 웹툰 EpisodeInfo API
 * Created by pluu on 2017-04-25.
 */
internal class KakaoEpisodeApi(
    private val networkUseCase: INetworkUseCase
) : EpisodeUseCase, INetworkUseCase by networkUseCase {

    override suspend fun invoke(param: EpisodeRequest): Result<EpisodeResult> {
        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val responseData = requestApi(
            createApi(id = param.toonId, page = param.page)
        ).mapJson()
            .let { result ->
                when (result) {
                    is Result.Success -> result.data
                    is Result.Error -> return Result.Error(result.exception)
                }
            }

        ///////////////////////////////////////////////////////////////////////////
        // Parse Data
        ///////////////////////////////////////////////////////////////////////////

        val list = responseData
            .getJSONArray("singles").let {
                parseList(param.toonId, it)
            }

        val episodePage = EpisodeResult(list)
        episodePage.nextLink = responseData.optBoolean("is_end", true).toString()

        if (param.page == 0) {
            episodePage.first = parseFirstKey(param.toonId)
                ?.let { firstId ->
                    list.firstOrNull()?.copy(id = firstId)
                }
        }

        return Result.Success(episodePage)
    }

    private fun parseList(toonId: String, array: JSONArray): List<EpisodeInfo> =
        array.asSequence()
            .map {
                EpisodeInfo(
                    id = it.optString("id"),
                    toonId = toonId,
                    title = it.optString("title"),
                    image = "https://dn-img-page.kakao.com/download/resource?kid=${it.optString("land_thumbnail_url")}&filename=th1",
                    updateDate = it.optString("free_change_dt")
                )
            }.toList()

    private suspend fun parseFirstKey(toonId: String): String? {
        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////
        val request = IRequest(
            method = REQUEST_METHOD.POST,
            url = "https://api2-page.kakao.com/api/v5/store/home",
            params = mapOf(
                "seriesid" to toonId
            )
        )

        return requestApi(request)
            .mapJson()
            .let { result ->
                when (result) {
                    is Result.Success -> result.data
                        .optString("first_single_id")
                        .takeIf { it.isNotEmpty() }
                    is Result.Error -> null
                }
            }
    }

    private fun createApi(id: String, page: Int): IRequest =
        IRequest(
            method = REQUEST_METHOD.POST,
            url = "https://api2-page.kakao.com/api/v5/store/singles",
            params = mapOf(
                "seriesid" to id,
                "page" to page.toString(),
                "direction" to "desc",
                "page_size" to "20",
                "without_hidden" to "true"
            )
        )
}
