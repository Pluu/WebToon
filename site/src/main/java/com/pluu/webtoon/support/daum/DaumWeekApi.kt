package com.pluu.webtoon.support.daum

import com.pluu.utils.asSequence
import com.pluu.utils.isNotEmpty
import com.pluu.webtoon.data.model.IRequest
import com.pluu.webtoon.data.model.REQUEST_METHOD
import com.pluu.webtoon.data.network.INetworkUseCase
import com.pluu.webtoon.data.network.mapJson
import com.pluu.webtoon.domain.usecase.WeeklyUseCase
import com.pluu.webtoon.domain.usecase.param.WeeklyRequest
import com.pluu.webtoon.model.Result
import com.pluu.webtoon.model.Status
import com.pluu.webtoon.model.ToonInfo
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 다음 웹툰 Week Api
 * Created by pluu on 2017-04-20.
 */
internal class DaumWeekApi(
    private val networkUseCase: INetworkUseCase
) : WeeklyUseCase, INetworkUseCase by networkUseCase {

    override val currentTabs = arrayOf("월", "화", "수", "목", "금", "토", "일")
    @Suppress("PrivatePropertyName")
    private val URL_VALUE = arrayOf("mon", "tue", "wed", "thu", "fri", "sat", "sun")

    override suspend fun invoke(param: WeeklyRequest): Result<List<ToonInfo>> {
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

        val array: JSONArray? = responseData
            .optJSONObject("data")
            ?.optJSONArray("webtoons")

        if (array?.isNotEmpty() != true) {
            return Result.Success(emptyList())
        }

        val today = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())

        val list = array.asSequence().map { obj ->
            val lastObj = obj.optJSONObject("latestWebtoonEpisode")
            val date = lastObj?.optString("dateCreated").orEmpty()
            createToon(obj, lastObj, date, today)
        }.toList()
        return Result.Success(list)
    }

    private fun createToon(
        obj: JSONObject,
        lastObj: JSONObject?,
        date: String,
        today: String
    ): ToonInfo {
        return ToonInfo(
            id = obj.optString("nickname"),
            title = obj.optString("title"),
            image = lastObj?.optJSONObject("thumbnailImage")?.optString("url").orEmpty(),
            writer = obj.optJSONObject("cartoon")
                ?.optJSONArray("artists")
                ?.optJSONObject(0)
                ?.optString("name").orEmpty(),
            rate = obj.optString("averageScore").toDoubleOrNull() ?: 0.0,
            updateDate = "${date.substring(2, 4)}.${date.substring(4, 6)}.${date.substring(6, 8)}",
            status = when {
                today == date.substring(
                    0,
                    8
                ) -> Status.UPDATE // 최근 업데이트
                "Y" == obj.optString("restYn") -> Status.BREAK // 휴재
                else -> Status.NONE
            },
            isAdult = obj.optInt("ageGrade") == 1
        )
    }

    private fun createApi(currentPos: Int): IRequest =
        IRequest(
            method = REQUEST_METHOD.POST,
            url = "http://m.webtoon.daum.net/data/mobile/webtoon",
            params = mapOf(
                "sort" to "update",
                "page_no" to "1",
                "week" to URL_VALUE[currentPos]
            )
        )
}
