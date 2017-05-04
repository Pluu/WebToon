package com.pluu.support.tstore

import android.content.Context
import com.pluu.support.impl.AbstractEpisodeApi
import com.pluu.support.impl.NetworkSupportApi
import com.pluu.webtoon.item.Episode
import com.pluu.webtoon.item.EpisodePage
import com.pluu.webtoon.item.WebToonInfo
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * TStore 웹툰 에피소드 Api
 * Created by pluu on 2017-04-27.
 */
class TStoreEpisodeApi(context: Context) : AbstractEpisodeApi(context) {

    private val EPISODE_ID = "(?<=prodId=)\\w+".toRegex()
    private val IMG_PATTERN = "(?<=url\\(').+(?='\\);)".toRegex()

    private lateinit var id: String
    private var firstEpisode: Episode? = null

    override fun parseEpisode(info: WebToonInfo): EpisodePage {
        this.id = info.toonId

        val episodePage = EpisodePage(this)

        val doc = try {
            Jsoup.parse(requestApi())
        } catch (e: Exception) {
            e.printStackTrace()
            return episodePage
        }

        firstEpisode = getFirstItem(info, doc)
        episodePage.episodes = parseList(info, doc)

        if (episodePage.episodes?.isNotEmpty() ?: false) {
            episodePage.nextLink = info.toonId
        }

        return episodePage
    }

    private fun parseList(info: WebToonInfo, doc: Document): List<Episode> {
        val list = mutableListOf<Episode>()
        doc.select(".layout-list-co ul li a").forEach { a ->
            val episodeId = EPISODE_ID.find(a.attr("href"))?.value ?: return@forEach

            Episode(info, episodeId).apply {
                image = a.select("span[class=list-thumbnail-pic ebook-lazy]")?.let {
                    IMG_PATTERN.find(it.attr("style"))?.value
                }
                episodeTitle = a.select(".list-item-text-title")?.text()
                updateDate = a.select(".list-item-text-date-ty1")?.text()
                list.add(this)
            }
        }
        return list
    }

    private fun getFirstItem(info: WebToonInfo, doc: Document) =
            doc.select(".layout-detail-header-btn a")?.attr("href")?.let {
                val result = EPISODE_ID.find(it)
                if (result != null) {
                    return@let Episode(info, result.value)
                } else {
                    return@let null
                }
            }

    override fun moreParseEpisode(item: EpisodePage) = item.nextLink

    override fun getFirstEpisode(item: Episode) = firstEpisode

    override val method: String
        get() = NetworkSupportApi.GET

    override val url: String
        get() = "http://m.tstore.co.kr/mobilepoc/webtoon/webtoonList.omp?prodId=$id"

}
