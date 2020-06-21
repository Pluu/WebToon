package com.pluu.webtoon.support.ktoon

import com.pluu.webtoon.data.model.IRequest
import com.pluu.webtoon.data.model.Result
import com.pluu.webtoon.data.network.INetworkUseCase
import com.pluu.webtoon.domain.moel.Status
import com.pluu.webtoon.domain.moel.ToonInfo
import com.pluu.webtoon.domain.usecase.WeeklyUseCase
import com.pluu.webtoon.domain.usecase.param.WeeklyRequest
import com.pluu.webtoon.network.mapDocument
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

/**
 * 올레 웹툰 Week API
 * Created by pluu on 2017-04-22.
 */
internal class KToonWeekApi(
    private val networkUseCase: INetworkUseCase
) : WeeklyUseCase, INetworkUseCase by networkUseCase {

    override val CURRENT_TABS = arrayOf("월", "화", "수", "목", "금", "토", "일")

    override suspend fun invoke(param: WeeklyRequest): Result<List<ToonInfo>> {
        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val responseData: Document = requestApi(createApi())
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

        val weekly = responseData.select("div[class=list week_all] .inner")
        val dataPosition = weekly.select("h4")
            .indexOfFirst { it.text() == CURRENT_TABS[param] }

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
