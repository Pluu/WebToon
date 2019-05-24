package com.pluu.webtoon.data.kakao

import com.pluu.kotlin.asSequence
import com.pluu.webtoon.data.IRequest
import com.pluu.webtoon.data.WeeklyRequest
import com.pluu.webtoon.data.impl.AbstractWeekApi
import com.pluu.webtoon.di.INetworkUseCase
import com.pluu.webtoon.item.Result
import com.pluu.webtoon.item.Status
import com.pluu.webtoon.item.ToonInfo
import com.pluu.webtoon.utils.safeApi
import org.json.JSONObject

/**
 * 카카오 페이지 웹툰 Week API
 * Created by pluu on 2017-04-25.
 */
class KakaoWeekApi(
    private val networkUseCase: INetworkUseCase
) : AbstractWeekApi, INetworkUseCase by networkUseCase {

    override val CURRENT_TABS = arrayOf("월", "화", "수", "목", "금", "토", "일")

    override suspend fun invoke(param: WeeklyRequest): Result<List<ToonInfo>> {
        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val responseData = requestApi(createApi(param)).safeApi { response ->
            JSONObject(response)
        }.let { result ->
            when (result) {
                is Result.Success -> result.data
                is Result.Error -> return Result.Error(result.exception)
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
                it.optJSONArray("section_series").asSequence()
            }?.flatMap {
                it.optJSONArray("list").asSequence()
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

    private fun createApi(currentPos: Int): IRequest = IRequest(
        url = "https://api2-page.kakao.com/api/v7/store/section_container/list",
        params = mapOf(
            "agent" to "web",
            "category" to "10",
            "subcategory" to "1000",
            "day" to (currentPos + 1).toString()
        )
    )
}
