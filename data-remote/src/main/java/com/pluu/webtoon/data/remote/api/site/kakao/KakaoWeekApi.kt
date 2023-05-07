package com.pluu.webtoon.data.remote.api.site.kakao

import com.pluu.utils.asSequence
import com.pluu.webtoon.data.remote.R
import com.pluu.webtoon.data.remote.api.WeeklyApi
import com.pluu.webtoon.data.remote.model.IRequest
import com.pluu.webtoon.data.remote.model.REQUEST_METHOD
import com.pluu.webtoon.data.remote.network.INetworkUseCase
import com.pluu.webtoon.data.remote.utils.ResourceLoader
import com.pluu.webtoon.data.remote.utils.mapJson
import com.pluu.webtoon.model.Result
import com.pluu.webtoon.model.Status
import com.pluu.webtoon.model.ToonInfo
import com.pluu.webtoon.model.WeekPosition
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.json.JSONObject
import javax.inject.Inject

/**
 * 카카오 페이지 웹툰 Week API
 * Created by pluu on 2017-04-25.
 */
internal class KakaoWeekApi @Inject constructor(
    private val networkUseCase: INetworkUseCase,
    private val resourceLoader: ResourceLoader
) : WeeklyApi, INetworkUseCase by networkUseCase {

    override val currentTabs = listOf("월", "화", "수", "목", "금", "토", "일")

    private val idRegex = "(?<=CardView-ItemSeries-)\\d+".toRegex()

    override suspend fun invoke(param: WeekPosition): Result<List<ToonInfo>> {
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

        return Result.Success(parse(responseData))
    }

    private fun parse(data: JSONObject): List<ToonInfo> {
        return data.optJSONObject("data")
            ?.optJSONObject("staticLandingDayOfWeekSection")
            ?.optJSONArray("groups")?.asSequence().orEmpty()
            .flatMap {
                it.optJSONArray("items")
                    ?.asSequence().orEmpty()
                    .mapNotNull { item ->
                        parseItem(item)
                    }
            }.toList()
    }

    private fun parseItem(json: JSONObject): ToonInfo? {
        val id = idRegex.find(json.optString("id"))?.value ?: return null
        return ToonInfo(
            id = id,
            title = json.optString("title"),
            image = "https:${json.optString("thumbnail")}",
            status = Status.NONE
        )
    }

    private fun createApi(currentPos: WeekPosition): IRequest =
        IRequest(
            method = REQUEST_METHOD.POST,
            url = "https://page.kakao.com/graphql",
            params = generateApiParams(
                dayTabUid = (currentPos.value + 1).toString(),
                pageNo = 1
            )
        )

    // TODO: Weekly에서 페이징 처리
    @Suppress("SameParameterValue")
    private fun generateApiParams(
        dayTabUid: String,
        pageNo: Int
    ): Map<String, String> {
        val query = resourceLoader.readRawResource(R.raw.kakao_weekly)
        val variables = buildJsonObject {
            put("sectionId", "static-landing-DayOfWeek-section-Layout-10-0-A-1-52")
            put("param", buildJsonObject {
                put("categoryUid", 10)
                put("screenUid", 52)
                put("dayTabUid", dayTabUid)
                put("page", pageNo)
            })
        }.toString()

        return mapOf(
            "query" to query,
            "variables" to variables
        )
    }
}
