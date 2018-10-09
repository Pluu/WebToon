package com.pluu.webtoon.data.naver

import com.pluu.webtoon.data.EpisodeRequest
import com.pluu.webtoon.data.IRequest
import com.pluu.webtoon.data.impl.AbstractEpisodeApi
import com.pluu.webtoon.di.INetworkUseCase
import com.pluu.webtoon.item.EpisodeInfo
import com.pluu.webtoon.item.EpisodeResult
import com.pluu.webtoon.item.Result
import com.pluu.webtoon.item.Status
import com.pluu.webtoon.utils.safeAPi
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

/**
 * 네이버 웹툰 EpisodeInfo API
 * Created by pluu on 2017-04-20.
 */
class NaverEpisodeApi(
    private val networkUseCase: INetworkUseCase
) : AbstractEpisodeApi, INetworkUseCase by networkUseCase {

    override fun invoke(param: EpisodeRequest): Result<EpisodeResult> {
        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val apiResult = safeAPi(
            requestApi(
                createApi(
                    id = param.toonId,
                    pageNo = param.page + 1
                )
            )
        ) { response ->
            Jsoup.parse(response)
        }

        val responseData = when (apiResult) {
            is Result.Success -> apiResult.data
            is Result.Error -> return Result.Error(apiResult.exception)
        }

        ///////////////////////////////////////////////////////////////////////////
        // Parse Data
        ///////////////////////////////////////////////////////////////////////////

        val episodePage = EpisodeResult(parseList(param.toonId, responseData))
        episodePage.nextLink = parsePage(responseData)
        episodePage.first = episodePage.episodes.firstOrNull()?.copy(id = "1")
        return Result.Success(episodePage)
    }

    private fun parseList(toonId: String, doc: Document): List<EpisodeInfo> {
        val pattern = "(?<=no=)\\d+".toRegex()
        return doc.select(".lst a")
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
        val info = element.select(".toon_info")
        return EpisodeInfo(
            id = episodeId,
            toonId = toonId,
            title = element.select(".toon_name").text(),
            image = element.select("img").first().attr("src"),
            rate = info.select("span[class=if1 st_r]").text(),
            status = when {
                info.select(".aside_info .ico_up").isNotEmpty() -> Status.UPDATE // 최근 업데이트
                info.select(".aside_info .ico_break").isNotEmpty() -> Status.BREAK // 휴재
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

    private fun createApi(id: String, pageNo: Int): IRequest = IRequest(
        url = "http://m.comic.naver.com/webtoon/list.nhn?titleId=$id&page=$pageNo"
    )
}
