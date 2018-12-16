package com.pluu.webtoon.data.kakao

import com.pluu.webtoon.data.IRequest
import com.pluu.webtoon.data.WeeklyRequest
import com.pluu.webtoon.data.impl.AbstractWeekApi
import com.pluu.webtoon.di.INetworkUseCase
import com.pluu.webtoon.item.Result
import com.pluu.webtoon.item.Status
import com.pluu.webtoon.item.ToonInfo
import com.pluu.webtoon.utils.safeAPi
import org.jsoup.Jsoup

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

        val apiResult = safeAPi(requestApi(createApi(param))) { response ->
            Jsoup.parse(response)
        }

        val responseData = when (apiResult) {
            is Result.Success -> apiResult.data
            is Result.Error -> return Result.Error(apiResult.exception)
        }

        ///////////////////////////////////////////////////////////////////////////
        // Parse Data
        ///////////////////////////////////////////////////////////////////////////

        val pattern = "(?<=/home/)\\d+(?=\\?categoryUid)".toRegex()
        val list = responseData.select(".list")
            .mapNotNull { element ->
                pattern.find(element.select("a").attr("href"))?.let {
                    ToonInfo(
                        id = it.value,
                        title = element.select(".title").first().text(),
                        image = element.select(".thumbnail img").last().attr("src"),
                        status = if (element.select(".badgeImg").isNotEmpty()) {
                            Status.UPDATE
                        } else {
                            Status.NONE
                        },
                        writer = element.select(".info").text().split("•").last().trim()
                    )
                }
            }
        return Result.Success(list)
    }

    private fun createApi(currentPos: Int): IRequest = IRequest(
        url = "http://page.kakao.com/main/ajaxCallWeeklyList",
        params = mapOf(
            "navi" to "1",
            "day" to (currentPos + 1).toString(),
            "inkatalk" to "0",
            "categoryUid" to "10",
            "subCategoryUid" to "1000"
        )
    )
}
