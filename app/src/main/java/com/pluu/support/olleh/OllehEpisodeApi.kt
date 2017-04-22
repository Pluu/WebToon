package com.pluu.support.olleh

import android.content.Context
import com.pluu.support.impl.AbstractEpisodeApi
import com.pluu.support.impl.NetworkSupportApi
import com.pluu.webtoon.item.Episode
import com.pluu.webtoon.item.EpisodePage
import com.pluu.webtoon.item.WebToonInfo
import com.pluu.webtoon.item.WebToonType
import org.json.JSONArray
import org.json.JSONObject

/**
 * 올레 웹툰 Episode API
 * Created by pluu on 2017-04-22.
 */
class OllehEpisodeApi(context: Context) : AbstractEpisodeApi(context) {
    private val PAGE_SIZE = 20

    private var savedArray: JSONArray? = null
    private var totalSize: Int = 0

    private var firstEpisode: Episode? = null
    private var page = 0

    private lateinit var id: String

    override fun parseEpisode(info: WebToonInfo): EpisodePage {
        id = info.toonId

        if (savedArray == null) {
            try {
                bindRequest(info)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val episodePage = EpisodePage(this).apply {
            episodes = parseList(info, savedArray, page)
            nextLink = getNextPageLink(totalSize, page)
        }
        page++
        return episodePage
    }

    @Throws(Exception::class)
    private fun bindRequest(info: WebToonInfo) {
        val response = requestApi()
        savedArray = JSONObject(response).optJSONArray("timesList")?.apply {
            totalSize = length()
            firstEpisode = createEpisode(info, optJSONObject(totalSize - 1))
        }
    }

    private fun parseList(info: WebToonInfo, array: JSONArray?, page: Int): List<Episode> {
        if (array == null) {
            return emptyList()
        }

        val list = mutableListOf<Episode>()
        val startPage = page * PAGE_SIZE
        var endPage = (page + 1) * PAGE_SIZE
        if (endPage > totalSize) {
            endPage = totalSize
        }

        (startPage until endPage)
                .mapTo(list) { createEpisode(info, array.optJSONObject(it)) }
        return list
    }

    private fun createEpisode(info: WebToonInfo, obj: JSONObject): Episode {
        return Episode(info, obj.optString("timesseq")).apply {
            episodeTitle = obj.optString("timestitle")
            image = if (info.type == WebToonType.TOON)
                obj.optString("thumbpath")
            else
                info.image
            rate = obj.optString("totalstickercnt")
        }
    }

    private fun getNextPageLink(totalSize: Int, current: Int): String? {
        val total = Math.ceil(totalSize / PAGE_SIZE.toDouble()).toInt()
        if (total >= current + 1) {
            // 다음 페이지 존재
            return EPISODE_URL
        }
        return null
    }

    override fun moreParseEpisode(item: EpisodePage): String {
        return item.nextLink
    }

    override fun getFirstEpisode(item: Episode): Episode? {
        return firstEpisode
    }

    override fun init() {
        super.init()
        savedArray = null
        page = 0
    }

    override val method: String
        get() = NetworkSupportApi.POST

    override val url: String
        get() = EPISODE_URL

    override val headers: Map<String, String>
        get() = hashMapOf("Referer" to OllehWeekApi.HOST)

    override val params: Map<String, String>
        get() = hashMapOf("webtoonseq" to id)

    companion object {
        private val EPISODE_URL = OllehWeekApi.HOST + "/api/work/getTimesListByWork.kt"
    }

}
