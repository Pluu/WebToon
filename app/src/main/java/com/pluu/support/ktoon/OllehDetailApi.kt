package com.pluu.support.ktoon

import android.content.Context
import android.net.Uri
import com.pluu.kotlin.asSequence
import com.pluu.support.impl.AbstractDetailApi
import com.pluu.support.impl.NetworkSupportApi
import com.pluu.webtoon.item.Detail
import com.pluu.webtoon.item.DetailView
import com.pluu.webtoon.item.Episode
import com.pluu.webtoon.item.ShareItem
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup

/**
 * 올레 웹툰 상세 API
 * Created by pluu on 2017-04-22.
 */
class OllehDetailApi(context: Context) : AbstractDetailApi(context) {

    private lateinit var wettonId: String
    private lateinit var timesseq: String

    override fun parseDetail(episode: Episode): Detail {
        this.wettonId = episode.toonId
        this.timesseq = episode.episodeId

        val ret = Detail().apply {
            webtoonId = episode.toonId
            episodeId = episode.episodeId
            title = episode.title
        }

        val array: JSONArray = try {
            JSONObject(requestApi()).optJSONArray("response")
        } catch (e: Exception) {
            e.printStackTrace()
            ret.list = emptyList()
            return ret
        }
        ret.list = parserToon(array)
        parsePrevNext().let {
            ret.prevLink = it.first
            ret.nextLink = it.second
        }
        return ret
    }

    private fun parserToon(array: JSONArray): List<DetailView> {
        return array.asSequence()
            .map {
                DetailView.createImage(it.optString("imagepath"))
            }
            .toList()
    }

    private fun parsePrevNext(): Pair<String?, String?> {
        val builder = Request.Builder().apply {
            val url =
                Uri.Builder().encodedPath("https://www.myktoon.com/mw/works/viewer.kt").apply {
                    appendQueryParameter("timesseq", timesseq)
                }.build().toString()
            url(url)
        }
        val pagingWrap = Jsoup.parse(requestApi(builder.build())).select(".paging_wrap")
        return Pair(
            pagingWrap.select("a[class=btn_prev moveViewerBtn]").attr("data-seq"),
            pagingWrap.select("a[class=btn_next moveViewerBtn]").attr("data-seq")
        )
    }

    override fun getDetailShare(episode: Episode, detail: Detail) = ShareItem(
        title = "${episode.title} / ${detail.title}",
        url = "https://v2.myktoon.com/mw/works/viewer.kt?timesseq=${detail.episodeId}"
    )

    override val url = "https://v2.myktoon.com/web/works/times_image_list_ajax.kt"

    override val method: String = NetworkSupportApi.POST

    override val params: Map<String, String>
        get() = hashMapOf("timesseq" to timesseq)
}
