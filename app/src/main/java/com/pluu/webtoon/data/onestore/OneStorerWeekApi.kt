package com.pluu.webtoon.data.onestore

import com.pluu.kotlin.asSequence
import com.pluu.kotlin.toFormatString
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
import java.util.*

/**
 * TStore 웹툰 Week Api
 * Created by pluu on 2017-04-27.
 */
class OneStorerWeekApi(
    private val networkUseCase: INetworkUseCase
) : AbstractWeekApi, INetworkUseCase by networkUseCase {

    override val CURRENT_TABS = arrayOf("월", "화", "수", "목", "금", "토", "일")
    private var startKey: String? = null

    override fun invoke(param: WeeklyRequest): Result<List<ToonInfo>> {
        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////
        val apiResult = safeAPi(
            requestApi(
                createApi(
                    position = param + 1, page = 0
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

        val array: JSONArray? = responseData
            .optJSONObject("webtoonVO")?.takeIf { vo ->
                vo.optString("hasNext") == "Y"
            }?.let { vo ->
                startKey = vo.optString("startKey")
                vo.optJSONArray("webtoonList")
            }

        val currentDate = Date().toFormatString("yyyyMMdd")
        val list = array?.asSequence()
            ?.map { createToon(it, currentDate) }
            ?.toList().orEmpty()
        return Result.Success(list)
    }

    private fun createToon(
        it: JSONObject,
        currentDate: String
    ): ToonInfo {
        return ToonInfo(
            id = it.optString("channelId"),
            title = it.optString("prodNm"),
            image = it.optString("filePos"),
            writer = it.optString("artistNm"),
            updateDate = it.optString("updateDate"),
            status = when (it.optString("statusCd")) {
                "continue" -> if (it.optString("updateDate") == currentDate) Status.UPDATE else Status.NONE
                "rest" -> Status.BREAK
                else -> Status.NONE
            },
            isAdult = it.optString("intentProdGrdCd") == "4"
        )
    }

    private fun createApi(position: Int, page: Int): IRequest = IRequest(
        method = REQUEST_METHOD.POST,
        url = when (page) {
            0 -> "http://m.onestore.co.kr/mobilepoc/webtoon/weekdayListDetail.omp"
            else -> "http://m.onestore.co.kr/mobilepoc/webtoon/weekdayListMore.omp"
        },
        headers = mapOf("Connection" to "close"),
        params = mutableMapOf(
            "Connection" to "close",
            "weekday" to position.toString()
        ).apply {
            startKey.takeIf { isNotEmpty() }?.let {
                put("startKey", it)
            }
        }
    )
}
