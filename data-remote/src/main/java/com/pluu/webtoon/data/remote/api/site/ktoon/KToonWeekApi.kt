package com.pluu.webtoon.data.remote.api.site.ktoon

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
 * 올레 웹툰 Week API
 * Created by pluu on 2017-04-22.
 */
internal class KToonWeekApi @Inject constructor(
    private val networkUseCase: INetworkUseCase
) : WeeklyApi, INetworkUseCase by networkUseCase {

    override val currentTabs = arrayOf("월", "화", "수", "목", "금", "토", "일")

    override suspend fun invoke(param: WeekPosition): Result<List<ToonInfo>> {
        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val responseData: Document = requestApi(createApi())
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

        val weekly = responseData.select("div[class=list week_all] .inner")
        val dataPosition = weekly.select("h4")
            .indexOfFirst { it.text() == currentTabs[param.value] }

        val list = weekly.getOrNull(dataPosition)
            ?.select("li")
            ?.mapNotNull { transform(it) }
            .orEmpty()

        return Result.Success(list)
    }

    private fun transform(item: Element): ToonInfo? {
        val directLink = item.select(".link").attr("href")
        val toonId = "(?<=worksseq=)\\w+".toRegex().find(directLink) ?: return null
        val info = item.select(".info")

        return ToonInfo(
            id = toonId.value,
            title = info.select("strong").text(),
            image = item.select(".thumb img").attr("src"),
            status = when {
                info.select(".ico_up").isNotEmpty() -> Status.UPDATE // 최근 업데이트트
                info.select(".ico_break").isNotEmpty() -> Status.BREAK // 휴재
                else -> Status.NONE
            },
            isAdult = info.select("ico_adult").isNotEmpty()
        )
    }

    private fun createApi(): IRequest =
        IRequest(
            url = "https://www.myktoon.com/web/webtoon/works_list.kt"
        )
}
