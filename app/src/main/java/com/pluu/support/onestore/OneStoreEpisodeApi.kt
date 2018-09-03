package com.pluu.support.onestore

import com.pluu.support.impl.AbstractEpisodeApi
import com.pluu.support.impl.REQUEST_METHOD
import com.pluu.webtoon.di.NetworkModule
import com.pluu.webtoon.item.Episode
import com.pluu.webtoon.item.EpisodePage
import com.pluu.webtoon.item.WebToonInfo
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * TStore 웹툰 에피소드 Api
 * Created by pluu on 2017-04-27.
 */
class OneStoreEpisodeApi(
    networkModule: NetworkModule
) : AbstractEpisodeApi(networkModule) {

    private val EPISODE_ID = "(?<=prodId=)\\w+".toRegex()

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

        if (episodePage.episodes?.isNotEmpty() == true) {
            episodePage.nextLink = info.toonId
        }

        return episodePage
    }

    private fun parseList(info: WebToonInfo, doc: Document): List<Episode> {
        val list = mutableListOf<Episode>()
        doc.select(".offering-card-link").forEach { item ->
            val episodeId = EPISODE_ID.find(item.attr("href"))?.value ?: return@forEach

            Episode(info, episodeId).apply {
                image = item.select(".thumbnail").attr("data-thumbackground")
                episodeTitle = item.select(".product-ti")?.text()
                updateDate = item.select(".product-product-date")?.text()
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

    override val method: REQUEST_METHOD = REQUEST_METHOD.GET

    override val url: String
        get() = "http://m.tstore.co.kr/mobilepoc/webtoon/webtoonList.omp?prodId=$id"

}
