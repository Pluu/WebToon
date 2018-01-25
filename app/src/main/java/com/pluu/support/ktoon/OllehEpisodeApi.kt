package com.pluu.support.ktoon

import android.content.Context
import com.pluu.support.impl.AbstractEpisodeApi
import com.pluu.support.impl.NetworkSupportApi
import com.pluu.webtoon.item.Episode
import com.pluu.webtoon.item.EpisodePage
import com.pluu.webtoon.item.WebToonInfo
import okhttp3.FormBody
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

/**
 * 올레 웹툰 Episode API
 * Created by pluu on 2017-04-22.
 */
class OllehEpisodeApi(context: Context) : AbstractEpisodeApi(context) {
    private val PAGE_SIZE = 20

    private var firstEpisode: Episode? = null
    private var pageNo = 0

    private lateinit var id: String

    override fun parseEpisode(info: WebToonInfo): EpisodePage {
        id = info.toonId

        val episodePage = EpisodePage(this)

        val array: JSONArray = try {
            JSONObject(requestApi()).optJSONArray("response")
        } catch (e: Exception) {
            e.printStackTrace()
            return episodePage
        }

        if (pageNo == 0) {
            firstEpisode = getFirstEpisode(info)
        }

        pageNo++

        episodePage.episodes = parseList(info, array)
        episodePage.nextLink = url.takeIf { array.length() >= PAGE_SIZE }
        return episodePage
    }

    private fun parseList(info: WebToonInfo, array: JSONArray): List<Episode> =
        (0 until array.length())
            .map { createEpisode(info, array.optJSONObject(it)) }

    private fun createEpisode(info: WebToonInfo, obj: JSONObject): Episode =
        Episode(info, obj.optString("timesseq")).apply {
            episodeTitle = obj.optString("timestitle")
            image = obj.optString("thumbpath")
        }

    private fun getFirstEpisode(info: WebToonInfo): Episode {
        val builder = Request.Builder().apply {
            val requestBody = FormBody.Builder().apply {
                mapOf(
                    "worksseq" to id,
                    "sorting" to "seq",
                    "turmCnt" to 1.toString()
                ).forEach {
                    add(it.key, it.value)
                }
            }.build()
            post(requestBody)
            url(EPISODE_URL)
        }
        val response = requestApi(builder.build())
        return createEpisode(info, JSONObject(response).optJSONArray("response").optJSONObject(0))
    }

    override fun moreParseEpisode(item: EpisodePage) = item.nextLink

    override fun getFirstEpisode(item: Episode) = firstEpisode

    override fun init() {
        super.init()
        pageNo = 0
    }

    override val method: String = NetworkSupportApi.POST

    override val url: String = EPISODE_URL

    override val params: Map<String, String>
        get() = hashMapOf(
            "worksseq" to id,
            "sorting" to "up",
            "startCnt" to (pageNo * PAGE_SIZE).toString(),
            "turmCnt" to PAGE_SIZE.toString()
        )

    companion object {
        private val EPISODE_URL = "https://v2.myktoon.com/web/works/times_list_ajax.kt"
    }
}
