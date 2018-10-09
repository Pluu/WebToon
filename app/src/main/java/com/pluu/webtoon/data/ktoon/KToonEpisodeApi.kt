package com.pluu.webtoon.data.ktoon

import com.pluu.kotlin.asSequence
import com.pluu.webtoon.data.EpisodeRequest
import com.pluu.webtoon.data.IRequest
import com.pluu.webtoon.data.REQUEST_METHOD
import com.pluu.webtoon.data.impl.AbstractEpisodeApi
import com.pluu.webtoon.di.INetworkUseCase
import com.pluu.webtoon.item.EpisodeInfo
import com.pluu.webtoon.item.EpisodeResult
import com.pluu.webtoon.item.Result
import com.pluu.webtoon.utils.safeAPi
import org.json.JSONArray
import org.json.JSONObject

/**
 * 올레 웹툰 EpisodeInfo API
 * Created by pluu on 2017-04-22.
 */
class KToonEpisodeApi(
    private val networkUseCase: INetworkUseCase
) : AbstractEpisodeApi, INetworkUseCase by networkUseCase {

    private val PAGE_SIZE = 20

    override fun invoke(param: EpisodeRequest): Result<EpisodeResult> {
        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val apiResult = safeAPi(
            requestApi(
                createApi(
                    id = param.toonId,
                    pageNo = param.page
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

        val array: JSONArray = responseData.optJSONArray("response")
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

    private fun getFirstEpisode(toonId: String): EpisodeInfo? {
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

        val apiResult = safeAPi(requestApi(request)) { response ->
            JSONObject(response)
        }

        val responseData = when (apiResult) {
            is Result.Success -> apiResult.data
            is Result.Error -> return null
        }

        ///////////////////////////////////////////////////////////////////////////
        // Parse Data
        ///////////////////////////////////////////////////////////////////////////

        val firstJson = responseData
            .optJSONArray("response")
            ?.optJSONObject(0) ?: return null

        return createEpisode(toonId, firstJson)
    }

    private fun createApi(id: String, pageNo: Int): IRequest = IRequest(
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
