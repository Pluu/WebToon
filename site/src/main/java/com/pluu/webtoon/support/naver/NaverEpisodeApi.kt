package com.pluu.webtoon.support.naver

import com.pluu.webtoon.data.model.IRequest
import com.pluu.webtoon.data.model.Result
import com.pluu.webtoon.data.network.INetworkUseCase
import com.pluu.webtoon.domain.base.AbstractEpisodeApi
import com.pluu.webtoon.domain.moel.EpisodeInfo
import com.pluu.webtoon.domain.moel.EpisodeResult
import com.pluu.webtoon.domain.moel.Status
import com.pluu.webtoon.domain.usecase.param.EpisodeRequest
import com.pluu.webtoon.network.mapDocument
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

/**
 * 네이버 웹툰 EpisodeInfo API
 * Created by pluu on 2017-04-20.
 */
class NaverEpisodeApi(
    private val networkUseCase: INetworkUseCase
) : AbstractEpisodeApi, INetworkUseCase by networkUseCase {

    override suspend fun invoke(param: EpisodeRequest): Result<EpisodeResult> {
        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val responseData: Document = requestApi(
            createApi(id = param.toonId, pageNo = param.page + 1)
        ).mapDocument()
            .let { result ->
                when (result) {
                    is Result.Success -> result.data
                    is Result.Error -> return Result.Error(result.exception)
                }
            }

        ///////////////////////////////////////////////////////////////////////////
        // Parse Data
        ///////////////////////////////////////////////////////////////////////////

        val episodePage =
            EpisodeResult(parseList(param.toonId, responseData))
        episodePage.nextLink = parsePage(responseData)
        episodePage.first = episodePage.episodes.firstOrNull()?.copy(id = "1")
        return Result.Success(episodePage)
    }

    private fun parseList(toonId: String, doc: Document): List<EpisodeInfo> {
        val pattern = "(?<=no=)\\d+".toRegex()
        return doc.select("li[data-title-id=$toonId] a")
            .mapNotNull { element ->
                pattern.find(element.attr("href"))?.let { matchResult ->
                    createEpisode(matchResult.value, element, toonId)
                }
            }
    }

    private fun createEpisode(
        episodeId: String,
        element: Element,
        toonId: String
    ): EpisodeInfo {
        val info = element.select(".info")
        return EpisodeInfo(
            id = episodeId,
            toonId = toonId,
            title = element.select(".name").text(),
            image = element.select("img").first().attr("src"),
            rate = info.select("detail score").text(),
            status = when {
                info.select("span[class=bullet up]").isNotEmpty() -> Status.UPDATE // 최근 업데이트
                info.select("span[class=bullet break]").isNotEmpty() -> Status.BREAK // 휴재
                else -> Status.NONE
            }
        )
    }

    private fun parsePage(doc: Document): String? {
        val nextPage = doc.select(".paging_type2 a[class=btn_next]")
        return when (nextPage.isNotEmpty()) {
            true -> 0.toString()
            else -> null
        }
    }

    private fun createApi(id: String, pageNo: Int): IRequest =
        IRequest(
            url = "https://m.comic.naver.com/webtoon/list.nhn?titleId=$id&page=$pageNo"
        )
}
