package com.pluu.webtoon.data.naver

import com.pluu.webtoon.common.Const
import com.pluu.webtoon.data.IRequest
import com.pluu.webtoon.data.WeeklyRequest
import com.pluu.webtoon.data.impl.AbstractWeekApi
import com.pluu.webtoon.di.INetworkUseCase
import com.pluu.webtoon.item.Result
import com.pluu.webtoon.item.Status
import com.pluu.webtoon.item.ToonInfo
import com.pluu.webtoon.utils.mapDocument
import org.jsoup.nodes.Element

/**
 * 네이버 웹툰 Week API
 * Created by pluu on 2017-04-20.
 */
class NaverWeekApi(
    private val networkUseCase: INetworkUseCase
) : AbstractWeekApi, INetworkUseCase by networkUseCase {

    override val CURRENT_TABS = TITLE

    private val URL_VALUE = arrayOf("mon", "tue", "wed", "thu", "fri", "sat", "sun", "fin")

    override suspend fun invoke(param: WeeklyRequest): Result<List<ToonInfo>> {
        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val responseData = requestApi(createApi(param))
            .mapDocument()
            .let { result ->
                when (result) {
                    is Result.Success -> result.data
                    is Result.Error -> return Result.Error(result.exception)
                }
            }

        ///////////////////////////////////////////////////////////////////////////
        // Parse Data
        ///////////////////////////////////////////////////////////////////////////

        val pattern = "(?<=titleId=)\\d+".toRegex()
        val list = responseData.select(".list_toon .item a")
            .mapNotNull { element ->
                pattern.find(element.attr("href"))
                    ?.takeIf { it.value.isNotEmpty() }
                    ?.let { matchResult ->
                        createToon(matchResult.value, element)
                    }
            }
        return Result.Success(list)
    }

    private fun createToon(
        id: String,
        element: Element
    ): ToonInfo {
        val info = element.select(".info")
        return ToonInfo(
            id = id,
            title = info.select(".title").text(),
            image = element.select("img").first().attr("src"),
            status = when {
                info.select("span[class=bullet up]").isNotEmpty() -> Status.UPDATE // 최근 업데이트
                info.select("span[class=bullet break]").isNotEmpty() -> Status.BREAK // 휴재
                else -> Status.NONE
            },
            isAdult = !element.select("em[class=badge badge_adult]").isEmpty(),
            writer = info.select(".author")?.first()?.text().orEmpty(),
            rate = element.select(".txt_score").text().toDoubleOrNull()?.let { rate ->
                Const.getRateNameByRate(rate)
            }.orEmpty(),
            updateDate = element.select("span[class=if1]").text()
        )
    }

    private fun createApi(pos: Int): IRequest = IRequest(
        url = "https://m.comic.naver.com/webtoon/weekday.nhn",
        params = mapOf("week" to URL_VALUE[pos])
    )

    companion object {
        private val TITLE = arrayOf("월", "화", "수", "목", "금", "토", "일", "완결")
    }
}
