package com.pluu.webtoon.data.remote.api.site.kakao

import com.pluu.utils.asSequence
import com.pluu.webtoon.data.remote.api.WeeklyApi
import com.pluu.webtoon.data.remote.model.IRequest
import com.pluu.webtoon.data.remote.network.INetworkUseCase
import com.pluu.webtoon.data.remote.utils.mapJson
import com.pluu.webtoon.model.Result
import com.pluu.webtoon.model.Status
import com.pluu.webtoon.model.ToonInfo
import com.pluu.webtoon.model.WeekPosition
import org.json.JSONObject
import javax.inject.Inject

/**
 * 카카오 페이지 웹툰 Week API
 * Created by pluu on 2017-04-25.
 */
internal class KakaoWeekApi @Inject constructor(
    private val networkUseCase: INetworkUseCase
) : WeeklyApi, INetworkUseCase by networkUseCase {

    override val currentTabs = arrayOf("월", "화", "수", "목", "금", "토", "일")

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
        return data.optJSONArray("section_containers")?.asSequence()
            ?.flatMap {
                it.optJSONArray("section_series")?.asSequence().orEmpty()
            }?.flatMap {
                it.optJSONArray("list")?.asSequence().orEmpty()
            }?.map {
                ToonInfo(
                    id = it.optString("series_id"),
                    title = it.optString("title"),
                    image = "https://dn-img-page.kakao.com/download/resource?kid=${it.optString("image")}&filename=th2",
                    updateDate = it.optString("last_slide_added_date"),
                    status = Status.NONE,
                    writer = it.optString("author")
                )
            }?.toList().orEmpty()
    }

    private fun createApi(currentPos: WeekPosition): IRequest =
        IRequest(
            url = "https://api2-page.kakao.com/api/v7/store/section_container/list",
            params = mapOf(
                "agent" to "web",
                "category" to "10",
                "subcategory" to "1000",
                "day" to (currentPos.value + 1).toString()
            )
        )
}
