package com.pluu.webtoon.data.remote.api.site.ktoon

import com.pluu.utils.asSequence
import com.pluu.utils.orEmpty
import com.pluu.webtoon.data.remote.api.EpisodeApi
import com.pluu.webtoon.data.remote.model.IRequest
import com.pluu.webtoon.data.remote.model.REQUEST_METHOD
import com.pluu.webtoon.data.remote.network.INetworkUseCase
import com.pluu.webtoon.data.remote.utils.mapJson
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.model.EpisodeResult
import com.pluu.webtoon.model.Result
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

/**
 * 올레 웹툰 EpisodeInfo API
 * Created by pluu on 2017-04-22.
 */
internal class KToonEpisodeApi @Inject constructor(
    private val networkUseCase: INetworkUseCase
) : EpisodeApi, INetworkUseCase by networkUseCase {

    @Suppress("PrivatePropertyName")
    private val PAGE_SIZE = 20

    override suspend fun invoke(param: EpisodeApi.Param): Result<EpisodeResult> {
        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val responseData = requestApi(
            createApi(id = param.toonId, pageNo = param.page)
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

        val array: JSONArray = responseData.optJSONArray("response").orEmpty()
        val episodePage = EpisodeResult(parseList(param.toonId, array))
        episodePage.nextLink = EPISODE_URL.takeIf { array.length() >= PAGE_SIZE }
        if (param.page == 0) {
            episodePage.first = getFirstEpisode(param.toonId)
        }
        return Result.Success(episodePage)
    }

    private fun parseList(toonId: String, array: JSONArray): List<EpisodeInfo> =
        array.asSequence()
            .map {
                createEpisode(toonId, it)
            }.toList()

    private fun createEpisode(toonId: String, it: JSONObject): EpisodeInfo {
        return EpisodeInfo(
            id = it.optString("timesseq"),
            toonId = toonId,
            title = it.optString("timestitle"),
            image = it.optString("thumbpath")
        )
    }

    private suspend fun getFirstEpisode(toonId: String): EpisodeInfo? {
        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////
        val request = IRequest(
            method = REQUEST_METHOD.POST,
            url = EPISODE_URL,
            params = mapOf(
                "worksseq" to toonId,
                "sorting" to "seq",
                "turmCnt" to 1.toString()
            )
        )

        val responseData = requestApi(request)
            .mapJson()
            .let { result ->
                when (result) {
                    is Result.Success -> result.data
                    else -> return null
                }
            }

        ///////////////////////////////////////////////////////////////////////////
        // Parse Data
        ///////////////////////////////////////////////////////////////////////////

        val firstJson = responseData
            .optJSONArray("response")
            ?.optJSONObject(0) ?: return null

        return createEpisode(toonId, firstJson)
    }

    private fun createApi(id: String, pageNo: Int): IRequest =
        IRequest(
            method = REQUEST_METHOD.POST,
            url = EPISODE_URL,
            params = mapOf(
                "worksseq" to id,
                "sorting" to "up",
                "startCnt" to (pageNo * PAGE_SIZE).toString(),
                "turmCnt" to PAGE_SIZE.toString()
            )
        )

    companion object {
        private const val EPISODE_URL = "https://v2.myktoon.com/web/works/times_list_ajax.kt"
    }
}
