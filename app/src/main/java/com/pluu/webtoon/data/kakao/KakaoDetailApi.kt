package com.pluu.webtoon.data.kakao

import com.pluu.kotlin.asSequence
import com.pluu.webtoon.data.DetailRequest
import com.pluu.webtoon.data.IRequest
import com.pluu.webtoon.data.REQUEST_METHOD
import com.pluu.webtoon.data.impl.AbstractDetailApi
import com.pluu.webtoon.di.INetworkUseCase
import com.pluu.webtoon.item.DetailResult
import com.pluu.webtoon.item.DetailView
import com.pluu.webtoon.item.Result
import com.pluu.webtoon.utils.mapJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject

/**
 * 카카오 페이지 웹툰 상세 API
 * Created by pluu on 2017-04-25.
 */
class KakaoDetailApi(
    private val networkUseCase: INetworkUseCase
) : AbstractDetailApi, INetworkUseCase by networkUseCase {

    override suspend fun invoke(param: DetailRequest): DetailResult {
        val id = param.episodeId

        return runBlocking {
            val json: JSONObject? = withContext(Dispatchers.Default) {
                getData(param.episodeId)
            }
            val prev: String? = withContext(Dispatchers.Default) {
                getMoreData(param.toonId, id, isPrev = true)
            }
            val next = withContext(Dispatchers.Default) {
                getMoreData(param.toonId, id, isPrev = false)
            }

            DetailResult.Detail(
                webtoonId = param.toonId,
                episodeId = id
            ).apply {
                title = param.episodeTitle
                list = json?.let { getImages(it) }.orEmpty()
                prevLink = prev
                nextLink = next
            }
        }
    }

    @Throws
    private suspend fun getData(id: String): JSONObject? {
        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        return requestApi(createApi(id))
            .mapJson()
            .let { result ->
                when (result) {
                    is Result.Success -> result.data
                    is Result.Error -> null
                }
            }
    }

    private fun getImages(json: JSONObject): List<DetailView>? {
        val memberInfo: JSONObject? =
            json.optJSONObject("downloadData")?.optJSONObject("members")
        return memberInfo?.optString("sAtsServerUrl")?.takeIf { it.isNotEmpty() }?.let { host ->
            memberInfo.optJSONArray("files")
                ?.asSequence()
                ?.map {
                    DetailView("$host${it.optString("secureUrl")}")
                }?.toList()
        }
    }

    private suspend fun getMoreData(toonId: String, episodeId: String, isPrev: Boolean): String? {
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

        val responseData = requestApi(request)
            .mapJson()
            .let { result ->
                when (result) {
                    is Result.Success -> result.data
                    is Result.Error -> return null
                }
            }

        ///////////////////////////////////////////////////////////////////////////
        // Parse Data
        ///////////////////////////////////////////////////////////////////////////

        return try {
            responseData
                .optJSONObject("item")?.takeIf {
                    it.optString("hidden", "N") == "N"
                }?.optString("pid")
                ?.replace("[^\\d]".toRegex(), "")
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun createApi(id: String): IRequest = IRequest(
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
