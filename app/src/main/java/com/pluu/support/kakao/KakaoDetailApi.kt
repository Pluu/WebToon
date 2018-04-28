package com.pluu.support.kakao

import android.content.Context
import com.pluu.kotlin.asSequence
import com.pluu.support.impl.AbstractDetailApi
import com.pluu.support.impl.REQUEST_METHOD
import com.pluu.webtoon.item.Detail
import com.pluu.webtoon.item.DetailView
import com.pluu.webtoon.item.Episode
import com.pluu.webtoon.item.ShareItem
import org.json.JSONObject

/**
 * 카카오 페이지 웹툰 상세 API
 * Created by pluu on 2017-04-25.
 */
class KakaoDetailApi(context: Context) : AbstractDetailApi(context) {

    private val DETAIL_URL =
        "http://page.kakao.com/viewer?productId=%s&categoryUid=10&subCategoryUid=0"
    private lateinit var id: String

    override fun parseDetail(episode: Episode): Detail {
        this.id = episode.episodeId

        val ret = Detail().apply {
            webtoonId = episode.toonId
        }

        val json: JSONObject = try {
            JSONObject(requestApi()).also {
                check(it.has("downloadData"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ret.list = emptyList()
            return ret
        }

        ret.apply {
            title = episode.title
            episodeId = id

            // TODO
//            prevLink = doc.select(".prevSectionBtn").attr("data-productid").takeIf {
//                "0" != it
//            }
            // TODO
//            nextLink = doc.select(".nextSectionBtn").attr("data-productid").takeIf {
//                "0" != it
//            }

            this.list = json.getJSONObject("downloadData")?.getJSONObject("members")?.let {
                val host = it.optString("sAtsServerUrl")
                it.getJSONArray("files")?.asSequence()?.map {
                    DetailView.createImage("$host${it.optString("secureUrl")}")
                }?.toList()
            }
        }
        return ret
    }

    override fun getDetailShare(episode: Episode, detail: Detail) = ShareItem(
        title = "${episode.title} / ${detail.title}",
        url = DETAIL_URL.format(episode.episodeId)
    )

    override val method: REQUEST_METHOD = REQUEST_METHOD.POST

    override val headers: Map<String, String> = mapOf(
        "User-Agent" to "Mozilla/5.0"
    )

    override val params: Map<String, String>
        get() = mapOf(
            "productId" to id
        )

    override val url: String = "https://api2-page.kakao.com/api/v1/inven/get_download_data/web"
}
