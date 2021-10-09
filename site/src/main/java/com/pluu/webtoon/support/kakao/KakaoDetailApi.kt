package com.pluu.webtoon.support.kakao

import com.pluu.utils.asSequence
import com.pluu.utils.orEmpty
import com.pluu.webtoon.data.model.IRequest
import com.pluu.webtoon.data.model.REQUEST_METHOD
import com.pluu.webtoon.data.network.INetworkUseCase
import com.pluu.webtoon.data.network.mapJson
import com.pluu.webtoon.domain.usecase.DetailUseCase
import com.pluu.webtoon.domain.usecase.param.DetailRequest
import com.pluu.webtoon.model.DetailResult
import com.pluu.webtoon.model.DetailView
import com.pluu.webtoon.model.successOr
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.json.JSONObject

/**
 * 카카오 페이지 웹툰 상세 API
 * Created by pluu on 2017-04-25.
 */
internal class KakaoDetailApi(
    private val networkUseCase: INetworkUseCase
) : DetailUseCase, INetworkUseCase by networkUseCase {

    override suspend fun invoke(param: DetailRequest): DetailResult {
        val id = param.episodeId

        return withContext(Dispatchers.Default) {
            val jsonDeferred = async {
                getData(param.episodeId).orEmpty()
            }
            val prevDeferred = async {
                getMoreData(param.toonId, id, isPrev = true).orEmpty()
            }
            val nextDeferred = async {
                getMoreData(param.toonId, id, isPrev = false).orEmpty()
            }

            DetailResult.Detail(
                webtoonId = param.toonId,
                episodeId = id,
                title = param.episodeTitle
            ).apply {
                list = getImages(jsonDeferred.await())
                prevLink = prevDeferred.await()
                nextLink = nextDeferred.await()
            }
        }
    }

    @Throws
    private suspend fun getData(id: String): JSONObject? {
        return requestApi(createApi(id))
            .mapJson()
            .successOr(null)
    }

    private fun getImages(json: JSONObject): List<DetailView> {
        val memberInfo: JSONObject? =
            json.optJSONObject("downloadData")?.optJSONObject("members")
        return memberInfo?.optString("sAtsServerUrl")?.takeIf { it.isNotEmpty() }?.let { host ->
            memberInfo.optJSONArray("files")
                ?.asSequence()
                ?.map { DetailView("$host${it.optString("secureUrl")}") }
                ?.toList()
        }.orEmpty()
    }

    private suspend fun getMoreData(toonId: String, episodeId: String, isPrev: Boolean): String? {
        return runCatching {
            getMoreResponse(isPrev, toonId, episodeId)
                ?.optJSONObject("item")?.takeIf {
                    it.optString("hidden", "N") == "N"
                }?.optString("pid")
                ?.replace("[^\\d]".toRegex(), "")
        }.getOrNull()
    }

    private suspend fun getMoreResponse(
        isPrev: Boolean,
        toonId: String,
        episodeId: String
    ): JSONObject? {
        val request = IRequest(
            method = REQUEST_METHOD.POST,
            url = if (isPrev) {
                "https://api2-page.kakao.com/api/v5/inven/get_prev_item"
            } else {
                "https://api2-page.kakao.com/api/v5/inven/get_next_item"
            },
            params = mapOf(
                "seriesPid" to toonId,
                "singlePid" to episodeId
            )
        )

        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        return requestApi(request)
            .mapJson()
            .successOr(null)
    }

    private fun createApi(id: String): IRequest =
        IRequest(
            method = REQUEST_METHOD.POST,
            url = "https://api2-page.kakao.com/api/v1/inven/get_download_data/web",
            params = mapOf(
                "productId" to id
            ),
            headers = mapOf(
                "User-Agent" to "Mozilla/5.0"
            )
        )
}
