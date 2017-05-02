package com.pluu.support.olleh

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
import org.json.JSONArray
import org.json.JSONObject

/**
 * 올레 웹툰 상세 API
 * Created by pluu on 2017-04-22.
 */
class OllehDetailApi(context: Context) : AbstractDetailApi(context) {

    override val url = OllehWeekApi.HOST + "/api/work/getTimesListByWork.kt"
    private val DETAIL_IMG_URL = OllehWeekApi.HOST + "/api/work/getTimesDetailImageList.kt"
    private val SHARE_URL = OllehWeekApi.HOST + "/web/times_view.kt?webtoonseq=%s&timesseq=%s"

    private lateinit var wettonId: String
    private lateinit var timesseq: String

    override fun parseDetail(episode: Episode): Detail {
        this.wettonId = episode.toonId
        this.timesseq = episode.episodeId

        val ret = Detail().apply {
            webtoonId = episode.toonId
        }

        val root = try {
            JSONObject(requestApi())
        } catch (e : Exception) {
            e.printStackTrace()
            ret.list = emptyList()
            return ret
        }

        val array : JSONArray? = root.optJSONArray("timesList")
        array?.apply {
            val timeSeq = Integer.valueOf(timesseq)
            for ((idx, obj) in iterator().withIndex()) {
                if (timeSeq == obj.optInt("timesseq")) {
                    ret.episodeId = obj.optString("timesseq")
                    ret.title = obj.optString("timestitle")
                    if (idx - 1 >= 0) {
                        ret.nextLink = array.optJSONObject(idx - 1).optInt("timesseq").toString()
                    }
                    if (idx + 1 < array.length()) {
                        ret.prevLink = array.optJSONObject(idx + 1).optInt("timesseq").toString()
                    }
                }
            }
        }

        ret.list = parserToon(requestImg)
        return ret
    }

    private val requestImg: JSONObject
        @Throws(Exception::class)
        get() {
            val builder = Request.Builder().url(DETAIL_IMG_URL).apply {
                for ((key, value) in headers) {
                    addHeader(key, value)
                }
                val formBuilder = FormBody.Builder().apply {
                    for ((key, value) in params) {
                        add(key, value)
                    }
                }
                post(formBuilder.build())
            }
            val response = requestApi(builder.build())
            return JSONObject(response)
        }

    private fun parserToon(path: JSONObject): List<DetailView> {
        val list = mutableListOf<DetailView>()
        path.optJSONArray("imageList")?.iterator()?.forEach { obj ->
            list.add(DetailView.createImage(obj.optString("imagepath")))
        }
        return list
    }

    override fun getDetailShare(episode: Episode, detail: Detail) = ShareItem(
        title = "${episode.title} / ${detail.title}",
        url = SHARE_URL.format(detail.webtoonId, detail.episodeId)
    )

    override val method: String
        get() = NetworkSupportApi.POST

    override val headers: Map<String, String>
        get() = hashMapOf("Referer" to OllehWeekApi.HOST)

    override val params: Map<String, String>
        get() = hashMapOf(
                "mobileyn" to "N",
                "webtoonseq" to wettonId,
                "timesseq" to timesseq
        )
}
