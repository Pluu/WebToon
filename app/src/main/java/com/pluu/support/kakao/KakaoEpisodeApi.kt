package com.pluu.support.kakao

import com.pluu.kotlin.asSequence
import com.pluu.support.impl.AbstractEpisodeApi
import com.pluu.support.impl.REQUEST_METHOD
import com.pluu.webtoon.di.NetworkModule
import com.pluu.webtoon.item.Episode
import com.pluu.webtoon.item.EpisodePage
import com.pluu.webtoon.item.WebToonInfo
import com.pluu.webtoon.utils.buildRequest
import com.pluu.webtoon.utils.toFormBody
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.withContext
import org.json.JSONArray
import org.json.JSONObject

/**
 * 카카오 페이지 웹툰 Episode API
 * Created by pluu on 2017-04-25.
 */
class KakaoEpisodeApi(
    networkModule: NetworkModule
) : AbstractEpisodeApi(networkModule) {

    private var firstEpisode: Episode? = null
    private lateinit var tooldId: String
    private var page: Int = 0
    private var isEnd: Boolean = false

    override fun parseEpisode(info: WebToonInfo): EpisodePage {
        tooldId = info.toonId
        val episodePage = EpisodePage(this)

        return runBlocking {
            val json: JSONObject? = withContext(Dispatchers.Default) {
                JSONObject(requestApi())
            }

            if (page == 0) {
                firstEpisode = withContext(Dispatchers.Default) {
                    parseFirstKey(tooldId)?.let {
                        createFirstEpisode(info, it)
                    }
                }
            }

            json ?: return@runBlocking episodePage

            isEnd = json.optBoolean("is_end", true)

            episodePage.episodes = parseList(info, json.getJSONArray("singles"))
            episodePage.nextLink = isEnd.takeIf { !it }?.toString()
            page++

            episodePage
        }
    }

    private fun createFirstEpisode(info: WebToonInfo, episodeId: String): Episode {
        return Episode(info, episodeId)
    }

    private fun parseList(info: WebToonInfo, array: JSONArray): List<Episode> =
        array.asSequence().map {
            Episode(info, it.optString("id")).apply {
                image =
                        "https://dn-img-page.kakao.com/download/resource?kid=${it.optString("land_thumbnail_url")}&filename=th1"
                episodeTitle = it.optString("title")
                updateDate = it.optString("free_change_dt")
            }
        }.toList()

    private fun parseFirstKey(toonId: String): String? {
        val request = buildRequest {
            post(
                mapOf(
                    "seriesid" to toonId
                ).toFormBody()
            )
            url("https://api2-page.kakao.com/api/v5/store/home")
        }
        return JSONObject(requestApi(request))
            .optString("first_single_id")
            ?.takeIf { it.isNotEmpty() }
    }

    override fun moreParseEpisode(item: EpisodePage): String? = isEnd.takeIf { !it }?.toString()

    override fun getFirstEpisode(item: Episode) = firstEpisode

    override fun init() {
        super.init()
        page = 1
    }

    override val url: String
        get() = "https://api2-page.kakao.com/api/v5/store/singles"

    override val method: REQUEST_METHOD = REQUEST_METHOD.POST

    override val params: Map<String, String>
        get() = mapOf(
            "seriesid" to tooldId,
            "page" to page.toString(),
            "direction" to "desc",
            "page_size" to "20",
            "without_hidden" to "true"
        )

}
