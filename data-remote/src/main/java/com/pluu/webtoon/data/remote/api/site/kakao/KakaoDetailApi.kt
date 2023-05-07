package com.pluu.webtoon.data.remote.api.site.kakao

import com.pluu.utils.asSequence
import com.pluu.webtoon.data.remote.R
import com.pluu.webtoon.data.remote.api.DetailApi
import com.pluu.webtoon.data.remote.model.IRequest
import com.pluu.webtoon.data.remote.model.REQUEST_METHOD
import com.pluu.webtoon.data.remote.network.INetworkUseCase
import com.pluu.webtoon.data.remote.utils.ResourceLoader
import com.pluu.webtoon.data.remote.utils.mapJson
import com.pluu.webtoon.model.DetailResult
import com.pluu.webtoon.model.DetailView
import com.pluu.webtoon.model.ERROR_TYPE
import com.pluu.webtoon.model.Result
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.json.JSONObject
import javax.inject.Inject

/**
 * 카카오 페이지 웹툰 상세 API
 * Created by pluu on 2017-04-25.
 */
internal class KakaoDetailApi @Inject constructor(
    private val networkUseCase: INetworkUseCase,
    private val resourceLoader: ResourceLoader
) : DetailApi, INetworkUseCase by networkUseCase {

    override suspend fun invoke(param: DetailApi.Param): DetailResult {
        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val responseData = requestApi(createApi(param.toonId, param.episodeId))
            .mapJson()
            .let { result ->
                when (result) {
                    is Result.Success -> result.data.getJSONObject("data")
                        .getJSONObject("viewerInfo")

                    is Result.Error -> return DetailResult.ErrorResult(
                        ERROR_TYPE.DEFAULT_ERROR(
                            result.throwable
                        )
                    )
                }
            }

        ///////////////////////////////////////////////////////////////////////////
        // Parse Data
        ///////////////////////////////////////////////////////////////////////////

        return DetailResult.Detail(
            webtoonId = param.toonId,
            episodeId = param.episodeId,
            title = param.episodeTitle
        ).apply {
            list = getImages(responseData)
            prevLink = responseData.optJSONObject("prevItem")?.optString("productId")
            nextLink = responseData.optJSONObject("nextItem")?.optString("productId")
        }
    }

    private fun getImages(json: JSONObject): List<DetailView> {
        return json.optJSONObject("viewerData")
            ?.optJSONObject("imageDownloadData")
            ?.optJSONArray("files")
            ?.asSequence().orEmpty()
            .map { item ->
                DetailView(item.getString("secureUrl"))
            }.toList()
    }

    private fun createApi(toonId: String, episodeId: String): IRequest =
        IRequest(
            method = REQUEST_METHOD.POST,
            url = "https://page.kakao.com/graphql",
            params = generateApiParams(
                seriesId = toonId,
                productId = episodeId
            )
        )

    private fun generateApiParams(
        seriesId: String,
        productId: String
    ): Map<String, String> {
        val query = resourceLoader.readRawResource(R.raw.kakao_detail)
        val variables = buildJsonObject {
            put("seriesId", seriesId)
            put("productId", productId)
        }.toString()

        return mapOf(
            "query" to query,
            "variables" to variables
        )
    }
}
