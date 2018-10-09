package com.pluu.webtoon.data.ktoon

import com.pluu.webtoon.data.IRequest
import com.pluu.webtoon.data.WeeklyRequest
import com.pluu.webtoon.data.impl.AbstractWeekApi
import com.pluu.webtoon.di.INetworkUseCase
import com.pluu.webtoon.item.Result
import com.pluu.webtoon.item.Status
import com.pluu.webtoon.item.ToonInfo
import com.pluu.webtoon.utils.safeAPi
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * 올레 웹툰 Week API
 * Created by pluu on 2017-04-22.
 */
class KToonWeekApi(
    private val networkUseCase: INetworkUseCase
) : AbstractWeekApi, INetworkUseCase by networkUseCase {

    override val CURRENT_TABS = arrayOf("월", "화", "수", "목", "금", "토", "일")

    override fun invoke(param: WeeklyRequest): Result<List<ToonInfo>> {
        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val apiResult = safeAPi(requestApi(createApi())) { response ->
            Jsoup.parse(response)
        }

        val responseData = when (apiResult) {
            is Result.Success -> apiResult.data
            is Result.Error -> return Result.Error(apiResult.exception)
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
        val toonId = "(?<=worksseq=).+".toRegex().find(directLink) ?: return null
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

    private fun createApi(): IRequest = IRequest(
        url = "https://www.myktoon.com/web/webtoon/works_list.kt"
    )
}
