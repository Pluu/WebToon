package com.pluu.support.tstore

import android.content.Context
import com.pluu.kotlin.iterator
import com.pluu.support.impl.AbstractDetailApi
import com.pluu.support.impl.NetworkSupportApi
import com.pluu.webtoon.item.Detail
import com.pluu.webtoon.item.DetailView
import com.pluu.webtoon.item.Episode
import com.pluu.webtoon.item.ShareItem
import okhttp3.FormBody
import okhttp3.Request
import org.json.JSONObject
import org.jsoup.Jsoup

/**
 * TStore 웹툰 상세 API
 * Created by pluu on 2017-04-27.
 */
class TStoreDetailApi(context: Context) : AbstractDetailApi(context) {

    private lateinit var id: String

    override fun parseDetail(episode: Episode): Detail {
        this.id = episode.episodeId

        val ret = Detail().apply {
            webtoonId = episode.toonId
            episodeId = id
        }

        val doc = try {
            Jsoup.parse(requestApi())
        } catch (e: Exception) {
            e.printStackTrace()
            ret.list = emptyList<DetailView>()
            return ret
        }

        ret.title = doc.select(".layout-detail-view-header .layout-detail-view-ti")?.text()

        ret.prevLink = doc.select("a[class=btn-episode-link btn-episode-prev]")?.attr("href")?.let {
            EPISODE_ID.find(it)?.value
        }
        ret.nextLink = doc.select("a[class=btn-episode-link btn-episode-next]")?.attr("href")?.let {
            EPISODE_ID.find(it)?.value
        }
        doc.select("#ifrmResizing")?.attr("src")?.let {
            CURRENT_ID.find(it)?.value?.apply {
                ret.list = getList(this)
            }
        }
        return ret
    }

    private fun getList(id: String): List<DetailView> {
        val request = Request.Builder().apply {
            url("https://www.myktoon.com/api/work/getTimesDetailImageList.kt")
            // POST
            val requestBody = FormBody.Builder().apply {
                add("timesseq", id)
            }.build()
            addHeader("Referer", "https://www.myktoon.com/web/open/build/work_view.kt")
            post(requestBody)
        }

        val list = mutableListOf<DetailView>()
        JSONObject(requestApi(request.build())).optJSONArray("imageList")?.iterator()?.forEach {
            list.add(DetailView.createImage(it.optString("imagepath")).apply {
                height = it.optInt("height").toFloat()
            })
        }
        return list
    }

    override fun getDetailShare(episode: Episode, detail: Detail) = ShareItem(
        title = "${episode.title} / ${detail.title}",
        url = "http://m.tstore.co.kr/mobilepoc/webtoon/webtoonDetail.omp?prodId=${detail.episodeId}&PrePageNm=/detail/webtoon/mw"
    )

    override val url: String
        get() = "http://m.onestore.co.kr/mobilepoc/webtoon/webtoonDetail.omp?prodId=$id&PrePageNm="

    override val method: String
        get() = NetworkSupportApi.GET

    companion object {
        private val EPISODE_ID = "(?<=ajaxWebtoonDetail\\(\\\\')[A-Za-z0-9]+".toRegex()
        private val CURRENT_ID = "(?<=timesseq=)\\d+".toRegex()
    }
}
