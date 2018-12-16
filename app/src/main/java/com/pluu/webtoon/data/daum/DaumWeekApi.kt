package com.pluu.webtoon.data.daum

import com.pluu.kotlin.asSequence
import com.pluu.kotlin.isNotEmpty
import com.pluu.webtoon.common.Const
import com.pluu.webtoon.data.IRequest
import com.pluu.webtoon.data.REQUEST_METHOD
import com.pluu.webtoon.data.WeeklyRequest
import com.pluu.webtoon.data.impl.AbstractWeekApi
import com.pluu.webtoon.di.INetworkUseCase
import com.pluu.webtoon.item.Result
import com.pluu.webtoon.item.Status
import com.pluu.webtoon.item.ToonInfo
import com.pluu.webtoon.utils.safeAPi
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

/**
 * 다음 웹툰 Week Api
 * Created by pluu on 2017-04-20.
 */
class DaumWeekApi(
    private val networkUseCase: INetworkUseCase
) : AbstractWeekApi, INetworkUseCase by networkUseCase {

    override val CURRENT_TABS = arrayOf("월", "화", "수", "목", "금", "토", "일")
    private val URL_VALUE = arrayOf("mon", "tue", "wed", "thu", "fri", "sat", "sun")

    override suspend fun invoke(param: WeeklyRequest): Result<List<ToonInfo>> {
        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val apiResult = safeAPi(requestApi(createApi(param))) { response ->
            JSONObject(response)
        }

        val responseData = when (apiResult) {
            is Result.Success -> apiResult.data
            is Result.Error -> return Result.Error(apiResult.exception)
        }

        ///////////////////////////////////////////////////////////////////////////
        // Parse Data
        ///////////////////////////////////////////////////////////////////////////

        val array: JSONArray? = responseData
            .optJSONObject("data")
            .optJSONArray("webtoons")

        if (array?.isNotEmpty() != true) {
            return Result.Success(emptyList())
        }

        val today = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())

        val list = array.asSequence().map { obj ->
            val lastObj = obj.optJSONObject("latestWebtoonEpisode")
            val date = lastObj.optString("dateCreated")

            ToonInfo(
                id = obj.optString("nickname"),
                title = obj.optString("title"),
                image = lastObj.optJSONObject("thumbnailImage").optString("url"),
                writer = obj.optJSONObject("cartoon")
                    .optJSONArray("artists")
                    .optJSONObject(0)
                    .optString("name"),
                rate = Const.getRateNameByRate(obj.optString("averageScore")).takeIf { it != "0.0" }.orEmpty(),
                updateDate =
                "${date.substring(2, 4)}.${date.substring(4, 6)}.${date.substring(6, 8)}",
                status = when {
                    today == date.substring(0, 8) -> Status.UPDATE // 최근 업데이트
                    "Y" == obj.optString("restYn") -> Status.BREAK // 휴재
                    else -> Status.NONE
                },
                isAdult = obj.optInt("ageGrade") == 1
            )
        }.toList()
        return Result.Success(list)
    }

    private fun createApi(currentPos: Int): IRequest = IRequest(
        method = REQUEST_METHOD.POST,
        url = "http://m.webtoon.daum.net/data/mobile/webtoon",
        params = mapOf(
            "sort" to "update",
            "page_no" to "1",
            "week" to URL_VALUE[currentPos]
        )
    )
}
