package com.pluu.webtoon.data.kakao

import com.pluu.kotlin.asSequence
import com.pluu.webtoon.data.EpisodeRequest
import com.pluu.webtoon.data.IRequest
import com.pluu.webtoon.data.REQUEST_METHOD
import com.pluu.webtoon.data.impl.AbstractEpisodeApi
import com.pluu.webtoon.di.NetworkUseCase
import com.pluu.webtoon.item.EpisodeInfo
import com.pluu.webtoon.item.EpisodeResult
import com.pluu.webtoon.item.Result
import com.pluu.webtoon.utils.safeAPi
import org.json.JSONArray
import org.json.JSONObject

/**
 * 카카오 페이지 웹툰 EpisodeInfo API
 * Created by pluu on 2017-04-25.
 */
class KakaoEpisodeApi(
    networkUseCase: NetworkUseCase
) : AbstractEpisodeApi(networkUseCase) {

    override fun parseEpisode(param: EpisodeRequest): Result<EpisodeResult> {
        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val apiResult = safeAPi(
            requestApi(
                createApi(
                    id = param.toonId,
                    page = param.page
                )
            )
        ) { response ->
            JSONObject(response)
        }

        val responseData = when (apiResult) {
            is Result.Success -> apiResult.data
            is Result.Error -> return Result.Error(apiResult.exception)
        }

        ///////////////////////////////////////////////////////////////////////////
        // Parse Data
        ///////////////////////////////////////////////////////////////////////////

        val list = responseData
            .getJSONArray("singles")
            ?.let {
                parseList(param.toonId, it)
            }.orEmpty()

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

    private fun parseFirstKey(toonId: String): String? {
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

        val apiResult = safeAPi(requestApi(request)) { response ->
            JSONObject(response)
        }

        ///////////////////////////////////////////////////////////////////////////
        // Parse Data
        ///////////////////////////////////////////////////////////////////////////

        return when (apiResult) {
            is Result.Success -> {
                apiResult.data
                    .optString("first_single_id")
                    ?.takeIf { it.isNotEmpty() }
            }
            is Result.Error -> null
        }
    }

    private fun createApi(id: String, page: Int): IRequest = IRequest(
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
