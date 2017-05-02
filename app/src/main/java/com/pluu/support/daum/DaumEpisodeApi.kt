package com.pluu.support.daum

import android.content.Context
import com.pluu.kotlin.isNotEmpty
import com.pluu.kotlin.iterator
import com.pluu.support.impl.AbstractEpisodeApi
import com.pluu.support.impl.NetworkSupportApi
import com.pluu.webtoon.item.Episode
import com.pluu.webtoon.item.EpisodePage
import com.pluu.webtoon.item.WebToonInfo
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

/**
 * 다음 웹툰 Episode API
 * Created by pluu on 2017-04-21.
 */
class DaumEpisodeApi(context: Context) : AbstractEpisodeApi(context) {

    private var name: String? = null

    private var firstEpisodeId: Int = 0
    private var pageNo = 0

    override fun parseEpisode(info: WebToonInfo): EpisodePage {
        this.name = info.toonId
        val episodePage = EpisodePage(this)

        val json: JSONObject = try {
            JSONObject(requestApi())
        } catch (e: Exception) {
            e.printStackTrace()
            return episodePage.apply {
                episodes = emptyList()
            }
        }

        val data = json.optJSONObject("data").optJSONArray("webtoonEpisodes")
        if (data?.isNotEmpty() ?: false) {
            episodePage.episodes = parseList(info, data)
        } else {
            episodePage.episodes = mutableListOf<Episode>()
        }
        val page = json.optJSONObject("page")

        val nick = info.toonId
        if (episodePage.episodes?.isNotEmpty() ?: false) {
            episodePage.nextLink = parsePage(page, nick)
        }

        if (firstEpisodeId == 0) {
            firstEpisodeId = getFirstEpisode(nick)
                    .optJSONObject("data").optInt("firstEpisodeId")
        }

        return episodePage
    }

    @Throws(Exception::class)
    private fun getFirstEpisode(nick: String): JSONObject {
        val builder = Request.Builder().url(PREFIX_FIRST_URL + nick)
        val response = requestApi(builder.build())
        return JSONObject(response)
    }

    private fun parseList(info: WebToonInfo, data: JSONArray): List<Episode> {
        val list = mutableListOf<Episode>()

        data.iterator().forEach { obj ->
            Episode(info, obj.optString("id")).apply {
                episodeTitle = obj.optString("title")
                image = obj.optJSONObject("thumbnailImage").optString("url")
                rate = obj.optJSONObject("voteTarget").optString("voteTotalScore")
                isLoginNeed = obj.optInt("price", 0) > 0
                updateDate = obj.optString("dateCreated")
                list.add(this)
            }
        }
        return list
    }

    private fun parsePage(obj: JSONObject, nickName: String): String? {
        val total = obj.optInt("size", 1)
        val current = obj.optInt("no", 1)
        if (total >= current + 1) {
            // 다음 페이지 존재
            pageNo = current + 1
            return nickName
        } else {
            // 끝
            return null
        }
    }

    override fun moreParseEpisode(item: EpisodePage) = item.nextLink

    override fun getFirstEpisode(item: Episode) = try {
        Episode(item).apply {
            episodeId = firstEpisodeId.toString()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    override fun init() {
        super.init()
        pageNo = 0
    }

    override val method: String
        get() = NetworkSupportApi.POST

    override val url: String
        get() = EPISODE_URL

    override val params: Map<String, String>
        get() = HashMap<String, String>().apply {
            put("page_size", "10")
            name?.let {
                put("nickname", it)
            }
            if (pageNo > 0) {
                put("page_no", pageNo.toString())
            }
        }

    companion object {

        private val EPISODE_URL = "http://m.webtoon.daum.net/data/mobile/webtoon/list_episode_by_nickname"
        private val PREFIX_FIRST_URL = "http://m.webtoon.daum.net/data/mobile/webtoon/view?nickname="
    }
}
