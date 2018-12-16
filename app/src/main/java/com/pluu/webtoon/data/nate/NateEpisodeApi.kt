package com.pluu.webtoon.data.nate

import com.pluu.kotlin.asSequence
import com.pluu.webtoon.data.EpisodeRequest
import com.pluu.webtoon.data.IRequest
import com.pluu.webtoon.data.REQUEST_METHOD
import com.pluu.webtoon.data.impl.AbstractEpisodeApi
import com.pluu.webtoon.di.INetworkUseCase
import com.pluu.webtoon.item.EpisodeInfo
import com.pluu.webtoon.item.EpisodeResult
import com.pluu.webtoon.item.Result
import com.pluu.webtoon.utils.safeAPi
import org.json.JSONArray
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * 네이트 웹툰 EpisodeInfo API
 * Created by pluu on 2017-04-27.
 */
class NateEpisodeApi(
    private val networkUseCase: INetworkUseCase
) : AbstractEpisodeApi, INetworkUseCase by networkUseCase {

    private val EPISODE_ID_PATTERN = "(?<=bsno=)\\d+".toRegex()

    override suspend fun invoke(param: EpisodeRequest): Result<EpisodeResult> {
        val isMorePage = param.page > 0

        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val apiResult = safeAPi(
            requestApi(
                createApi(
                    id = param.toonId,
                    pageNo = param.page
                )
            )
        ) { response ->
            @Suppress("IMPLICIT_CAST_TO_ANY")
            if (isMorePage) {
                Jsoup.parse(response)
            } else {
                JSONArray(response)
            }
        }

        val responseData = when (apiResult) {
            is Result.Success -> apiResult.data
            is Result.Error -> return Result.Error(apiResult.exception)
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

    private fun createApi(id: String, pageNo: Int): IRequest = IRequest(
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
