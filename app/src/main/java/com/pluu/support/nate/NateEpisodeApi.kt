package com.pluu.support.nate

import android.content.Context
import com.pluu.kotlin.iterator
import com.pluu.support.impl.AbstractEpisodeApi
import com.pluu.support.impl.NetworkSupportApi
import com.pluu.webtoon.item.Episode
import com.pluu.webtoon.item.EpisodePage
import com.pluu.webtoon.item.WebToonInfo
import org.json.JSONArray
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * 네이트 웹툰 Episode API
 * Created by pluu on 2017-04-27.
 */
class NateEpisodeApi(context: Context) : AbstractEpisodeApi(context) {

    private val EPISODE_ID_PATTERN = "(?<=bsno=)\\d+".toRegex()

    override lateinit var url: String

    private var firstEpisode: Episode? = null
    private var pageNo: Int = 0

    override fun parseEpisode(info: WebToonInfo): EpisodePage {
        val isMorePage = pageNo > 0

        if (isMorePage) {
            this.url = "http://m.comics.nate.com/main2/webtoon/WebtoonInfoMore.php?btno=${info.toonId}&page=$pageNo"
        } else {
            this.url = "http://m.comics.nate.com/main/info?btno=${info.toonId}&order=up"
        }

        val episodePage = EpisodePage(this)

        val response = try {
            requestApi()
        } catch (e: Exception) {
            e.printStackTrace()
            return episodePage
        }

        val doc = Jsoup.parse(response)
        episodePage.episodes = if (isMorePage) {
            parseList(info, JSONArray(response))
        } else {
            if (firstEpisode == null) {
                doc.select(".tp").first()?.attr("href")?.apply {
                    EPISODE_ID_PATTERN.find(this)?.apply {
                        firstEpisode = Episode(info, value)
                    }
                }
            }
            parseList(info, doc)
        }

        if (episodePage.episodes.isNotEmpty()) {
            episodePage.nextLink = info.toonId
        }

        pageNo++

        return episodePage
    }

    private fun parseList(info: WebToonInfo, doc: Document): List<Episode> {
        val list = mutableListOf<Episode>()
        doc.select(".first").forEach {
            EPISODE_ID_PATTERN.find(it.select("a").first().attr("href"))?.apply {
                Episode(info, value).apply {
                    image = it.select("img").first().attr("src")
                    episodeTitle = it.select(".tel_episode").text() + " " + it.select(".tel_title").text()
                    updateDate = it.select(".tel_date").text()
                    list.add(this)
                }
            }
        }
        return list
    }

    private fun parseList(info: WebToonInfo, array: JSONArray): List<Episode> {
        val list = mutableListOf<Episode>()
        array.iterator().forEach { obj ->
            Episode(info, obj.optString("bsno")).apply {
                image = obj.optString("thumb")
                episodeTitle = obj.optString("volume_txt") + " " + obj.optString("sub_title")
                updateDate = obj.optString("last_update")
                list.add(this)
            }
        }
        return list
    }

    override fun moreParseEpisode(item: EpisodePage): String = item.nextLink

    override fun getFirstEpisode(item: Episode): Episode? = firstEpisode

    override fun init() {
        super.init()
        pageNo = 1
    }

    override val method: String
        get() {
            if (pageNo > 1) {
                return NetworkSupportApi.POST
            } else {
                return NetworkSupportApi.GET
            }
        }

}
