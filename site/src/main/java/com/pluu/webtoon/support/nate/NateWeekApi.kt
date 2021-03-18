package com.pluu.webtoon.support.nate

import com.pluu.webtoon.data.model.IRequest
import com.pluu.webtoon.data.network.INetworkUseCase
import com.pluu.webtoon.domain.usecase.WeeklyUseCase
import com.pluu.webtoon.domain.usecase.param.WeeklyRequest
import com.pluu.webtoon.model.Result
import com.pluu.webtoon.model.ToonInfo
import com.pluu.webtoon.network.mapDocument
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

/**
 * 네이트 웹툰 Week API
 * Created by pluu on 2017-04-26.
 */
internal class NateWeekApi(
    private val networkUseCase: INetworkUseCase
) : WeeklyUseCase, INetworkUseCase by networkUseCase {

    override val currentTabs = arrayOf("월", "화", "수", "목", "금", "토", "일")

    override suspend fun invoke(param: WeeklyRequest): Result<List<ToonInfo>> {
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

        val pattern = "(?<=btno=)\\d+".toRegex()
        val list = responseData.select(".wkTypeAll_$param")
            .mapNotNull { element ->
                pattern.find(element.attr("href"))?.let { matchResult ->
                    createToon(matchResult.value, element)
                }
            }
        return Result.Success(list)
    }

    private fun createToon(
        id: String,
        element: Element
    ): ToonInfo {
        return ToonInfo(
            id = id,
            title = element.select(".wtl_title").text(),
            image = element.select(".wtl_img img").first().attr("src"),
            writer = element.select(".wtl_author").text()
        )
    }

    private fun createApi(): IRequest =
        IRequest(
            url = "http://m.comics.nate.com/main/index"
        )
}
