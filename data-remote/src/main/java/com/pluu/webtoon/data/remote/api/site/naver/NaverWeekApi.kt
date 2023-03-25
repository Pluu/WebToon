package com.pluu.webtoon.data.remote.api.site.naver

import com.pluu.webtoon.data.remote.api.WeeklyApi
import com.pluu.webtoon.data.remote.model.IRequest
import com.pluu.webtoon.data.remote.network.INetworkUseCase
import com.pluu.webtoon.data.remote.utils.mapDocument
import com.pluu.webtoon.model.Result
import com.pluu.webtoon.model.Status
import com.pluu.webtoon.model.ToonInfo
import com.pluu.webtoon.model.WeekPosition
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import javax.inject.Inject

/**
 * 네이버 웹툰 Week API
 * Created by pluu on 2017-04-20.
 */
internal class NaverWeekApi @Inject constructor(
    private val networkUseCase: INetworkUseCase
) : WeeklyApi, INetworkUseCase by networkUseCase {

    override val currentTabs = TITLE

    @Suppress("PrivatePropertyName")
    private val URL_VALUE = arrayOf("mon", "tue", "wed", "thu", "fri", "sat", "sun", "fin")

    override suspend fun invoke(param: WeekPosition): Result<List<ToonInfo>> {
        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val responseData: Document = requestApi(createApi(param))
            .mapDocument()
            .let { result ->
                when (result) {
                    is Result.Success -> result.data
                    is Result.Error -> return Result.Error(result.throwable)
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
            image = element.select("img").first()?.attr("src").orEmpty(),
            status = when {
                info.select("span[class=bullet up]").isNotEmpty() -> Status.UPDATE // 최근 업데이트
                info.select("span[class=bullet break]").isNotEmpty() -> Status.BREAK // 휴재
                else -> Status.NONE
            },
            isAdult = !element.select("em[class=badge badge_adult]").isEmpty(),
            writer = info.select(".author").first()?.text().orEmpty(),
            rate = element.select(".txt_score").text().toDoubleOrNull() ?: 0.0,
            updateDate = element.select("span[class=if1]").text()
        )
    }

    private fun createApi(pos: WeekPosition): IRequest =
        IRequest(
            url = "https://m.comic.naver.com/webtoon/weekday.nhn",
            params = mapOf("week" to URL_VALUE[pos.value])
        )

    companion object {
        private val TITLE = listOf("월", "화", "수", "목", "금", "토", "일", "완결")
    }
}
