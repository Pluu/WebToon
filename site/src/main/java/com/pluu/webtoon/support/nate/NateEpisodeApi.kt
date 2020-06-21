package com.pluu.webtoon.support.nate

import com.pluu.core.asSequence
import com.pluu.webtoon.data.model.IRequest
import com.pluu.webtoon.data.model.REQUEST_METHOD
import com.pluu.webtoon.data.model.Result
import com.pluu.webtoon.data.network.INetworkUseCase
import com.pluu.webtoon.data.network.safeApi
import com.pluu.webtoon.domain.moel.EpisodeInfo
import com.pluu.webtoon.domain.moel.EpisodeResult
import com.pluu.webtoon.domain.usecase.EpisodeUseCase
import com.pluu.webtoon.domain.usecase.param.EpisodeRequest
import org.json.JSONArray
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * 네이트 웹툰 EpisodeInfo API
 * Created by pluu on 2017-04-27.
 */
class NateEpisodeApi(
    private val networkUseCase: INetworkUseCase
) : EpisodeUseCase, INetworkUseCase by networkUseCase {

    private val EPISODE_ID_PATTERN = "(?<=bsno=)\\d+".toRegex()

    override suspend fun invoke(param: EpisodeRequest): Result<EpisodeResult> {
        val isMorePage = param.page > 0

        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val responseData = requestApi(
            createApi(
                id = param.toonId,
                pageNo = param.page
            )
        ).safeApi { response ->
            @Suppress("IMPLICIT_CAST_TO_ANY")
            if (isMorePage) {
                Jsoup.parse(response)
            } else {
                JSONArray(response)
            }
        }.let { result ->
            when (result) {
                is Result.Success -> result.data
                is Result.Error -> return Result.Error(result.exception)
            }
        }

        ///////////////////////////////////////////////////////////////////////////
        // Parse Data
        ///////////////////////////////////////////////////////////////////////////

        var firstId: String? = null
        val episodes = if (isMorePage) {
            parseList(param.toonId, responseData as JSONArray)
        } else {
            val doc = responseData as Document
            firstId = doc.select(".tp").first()?.attr("href")?.let { first ->
                EPISODE_ID_PATTERN.find(first)?.value
            }
            parseList(param.toonId, doc)
        }

        val episodePage = EpisodeResult(episodes)
        if (episodePage.episodes.isNotEmpty()) {
            if (firstId != null) {
                episodePage.episodes.first().copy(id = firstId)
            }
            episodePage.nextLink = param.toonId
        }

        return Result.Success(episodePage)
    }

    private fun parseList(toonId: String, doc: Document): List<EpisodeInfo> {
        val list = mutableListOf<EpisodeInfo>()
        doc.select(".first").map {
            EPISODE_ID_PATTERN.find(it.select("a").first().attr("href"))?.apply {
                EpisodeInfo(
                    id = value,
                    toonId = toonId,
                    image = it.select("img").first().attr("src"),
                    title = it.select(".tel_episode").text() + " " +
                            it.select(".tel_title").text(),
                    updateDate = it.select(".tel_date").text()
                ).also { item ->
                    list.add(item)
                }
            }
        }
        return list
    }

    private fun parseList(toonId: String, array: JSONArray): List<EpisodeInfo> {
        return array.asSequence()
            .map { obj ->
                EpisodeInfo(
                    id = obj.optString("bsno"),
                    toonId = toonId,
                    image = obj.optString("thumb"),
                    title = obj.optString("volume_txt") + " " + obj.optString("sub_title"),
                    updateDate = obj.optString("last_update")
                )
            }.toList()
    }

    private fun createApi(id: String, pageNo: Int): IRequest =
        IRequest(
            method = if (pageNo > 1) {
                REQUEST_METHOD.POST
            } else {
                REQUEST_METHOD.GET
            },
            url = if (pageNo > 0) {
                "http://m.comics.nate.com/main2/webtoon/WebtoonInfoMore.php?btno=$id&page=$pageNo"
            } else {
                "http://m.comics.nate.com/main/info?btno=$id&order=up"
            }
        )
}
