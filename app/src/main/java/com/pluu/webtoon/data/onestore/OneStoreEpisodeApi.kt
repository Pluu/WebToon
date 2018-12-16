package com.pluu.webtoon.data.onestore

import com.pluu.webtoon.data.EpisodeRequest
import com.pluu.webtoon.data.IRequest
import com.pluu.webtoon.data.impl.AbstractEpisodeApi
import com.pluu.webtoon.di.INetworkUseCase
import com.pluu.webtoon.item.EpisodeInfo
import com.pluu.webtoon.item.EpisodeResult
import com.pluu.webtoon.item.Result
import com.pluu.webtoon.utils.safeAPi
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

/**
 * TStore 웹툰 에피소드 Api
 * Created by pluu on 2017-04-27.
 */
class OneStoreEpisodeApi(
    private val networkUseCase: INetworkUseCase
) : AbstractEpisodeApi, INetworkUseCase by networkUseCase {

    private val EPISODE_ID = "(?<=prodId=)\\w+".toRegex()

    override suspend fun invoke(param: EpisodeRequest): Result<EpisodeResult> {
        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val apiResult = safeAPi(requestApi(createApi(param.toonId))) { response ->
            Jsoup.parse(response)
        }

        val responseData = when (apiResult) {
            is Result.Success -> apiResult.data
            is Result.Error -> return Result.Error(apiResult.exception)
        }

        ///////////////////////////////////////////////////////////////////////////
        // Parse Data
        ///////////////////////////////////////////////////////////////////////////

        return EpisodeResult(parseList(param.toonId, responseData)).apply {
            nextLink = param.toonId.takeIf { episodes.isNotEmpty() }
            first = getFirstId(responseData)?.let { firstId ->
                episodes.firstOrNull()?.copy(id = firstId)
            }
        }.let {
            Result.Success(it)
        }
    }

    private fun parseList(toonId: String, doc: Document): List<EpisodeInfo> {
        return doc.select(".offering-card-link")
            .mapNotNull { element ->
                EPISODE_ID.find(element.attr("href"))?.value?.let { episodeId ->
                    createEpisode(toonId, episodeId, element)
                }
            }
    }

    private fun createEpisode(toonId: String, episodeId: String, item: Element): EpisodeInfo {
        return EpisodeInfo(
            id = episodeId,
            toonId = toonId,
            title = item.select(".product-ti")?.text().orEmpty(),
            image = item.select(".thumbnail").attr("data-thumbackground"),
            updateDate = item.select(".product-product-date")?.text().orEmpty()
        )
    }

    private fun getFirstId(doc: Document): String? =
        doc.select(".layout-detail-header-btn a")?.attr("href")?.let {
            EPISODE_ID.find(it)?.value
        }

    private fun createApi(id: String): IRequest = IRequest(
        url = "http://m.tstore.co.kr/mobilepoc/webtoon/webtoonList.omp?prodId=$id"
    )
}
