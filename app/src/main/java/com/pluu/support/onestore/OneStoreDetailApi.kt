package com.pluu.support.onestore

import android.content.Context
import com.pluu.kotlin.asSequence
import com.pluu.support.impl.AbstractDetailApi
import com.pluu.support.impl.REQUEST_METHOD
import com.pluu.webtoon.item.Detail
import com.pluu.webtoon.item.DetailView
import com.pluu.webtoon.item.Episode
import com.pluu.webtoon.item.ShareItem
import okhttp3.FormBody
import okhttp3.Request
import org.json.JSONArray
import org.jsoup.Jsoup

/**
 * TStore 웹툰 상세 API
 * Created by pluu on 2017-04-27.
 */
class OneStoreDetailApi(context: Context) : AbstractDetailApi(context) {

    private lateinit var id: String

    override fun parseDetail(episode: Episode): Detail {
        this.id = episode.episodeId

        val ret = Detail().apply {
            webtoonId = episode.toonId
            episodeId = id
        }

        val array: JSONArray = try {
            val doc = Jsoup.parse(requestApi())
            ret.title = doc.select(".detail-episode-header-ti").text()

            val timesseq = doc.select("#ifrmResizing").attr("src")?.let {
                CURRENT_ID.find(it)?.value
            } ?: throw IllegalStateException("Not Found Key")

            ret.prevLink = doc.select("[class=btn-detail-episode-btn btn-detail-episode-prev]")
                .attr("href")?.let {
                EPISODE_ID.find(it)?.value
            }
            ret.nextLink = doc.select("[class=btn-detail-episode-btn btn-detail-episode-next]")
                .attr("href")?.let {
                EPISODE_ID.find(it)?.value
            }

            // Image List
            val builder = Request.Builder().apply {
                val requestBody = FormBody.Builder().apply {
                    mapOf("timesseq" to timesseq).forEach {
                        add(it.key, it.value)
                    }
                }.build()
                post(requestBody)
                url("https://v2.myktoon.com/mw/open/onestore/getTimesDetailImageList.kt")
            }
            JSONArray(requestApi(builder.build()))
        } catch (e: Exception) {
            e.printStackTrace()
            ret.list = emptyList()
            return ret
        }

        ret.list = array.asSequence()
            .map {
                DetailView.createImage(it.optString("imagepath")).apply {
                    height = it.optInt("height").toFloat()
                }
            }
            .toList()
        return ret
    }

    override fun getDetailShare(episode: Episode, detail: Detail) = ShareItem(
        title = "${episode.title} / ${detail.title}",
        url = "http://m.tstore.co.kr/mobilepoc/webtoon/webtoonDetail.omp?prodId=${detail.episodeId}&PrePageNm=/detail/webtoon/mw"
    )

    override val url: String
        get() = "http://m.onestore.co.kr/mobilepoc/webtoon/webtoonDetail.omp?prodId=$id&PrePageNm="

    override val method: REQUEST_METHOD = REQUEST_METHOD.GET

    companion object {
        private val EPISODE_ID = "(?<=')[A-Za-z0-9]+".toRegex()
        private val CURRENT_ID = "(?<=timesseq=)\\d+".toRegex()
    }
}
