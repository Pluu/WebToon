package com.pluu.support.daum

import android.content.Context
import com.pluu.kotlin.iterator
import com.pluu.support.impl.AbstractDetailApi
import com.pluu.support.impl.REQUEST_METHOD
import com.pluu.webtoon.item.*
import org.json.JSONObject

/**
 * 다음 웹툰 상세 API
 * Created by pluu on 2017-04-22.
 */
class DaumDetailApi(context: Context) : AbstractDetailApi(context) {
    private lateinit var id: String

    override fun parseDetail(episode: Episode): Detail {
        this.id = episode.episodeId

        val ret = Detail().apply {
            webtoonId = episode.toonId
        }

        val json = try {
            JSONObject(requestApi()).optJSONObject("data")
        } catch (e: Exception) {
            return ret
        }

        if (json.optJSONObject("webtoonEpisode")?.optInt("price", 0) ?: 0 > 0) {
            ret.errorType = ERROR_TYPE.COIN_NEED
            return ret
        }

        ret.apply {
            json.optJSONObject("webtoonEpisode").apply {
                title = optString("title")
                episodeId = optString("id")
            }

            val nextId = json.optInt("nextEpisodeId", 0)
            val prevId = json.optInt("prevEpisodeId", 0)
            if (nextId > 0) {
                nextLink = nextId.toString()
            }
            if (prevId > 0) {
                prevLink = prevId.toString()
            }

            list = defaultDetailParse(json)
        }
        return ret
    }

    private fun defaultDetailParse(json: JSONObject): List<DetailView> {
        val list = mutableListOf<DetailView>()
        json.optJSONArray("webtoonImages")?.iterator()?.forEach { it ->
            list.add(DetailView(it.optString("url")))
        }
        json.optJSONArray("webtoonEpisodePages")?.iterator()?.forEach { it ->
            list.add(
                DetailView(
                    it.optJSONArray("webtoonEpisodePageMultimedias")
                        .optJSONObject(0)
                        .optJSONObject("image")
                        .optString("url")
                )
            )
        }
        return list
    }

    override fun getDetailShare(episode: Episode, detail: Detail) = ShareItem(
        title = "${episode.title} / ${detail.title}",
        url = "$SHARE_URL$detail.episodeId"
    )

    override val method: REQUEST_METHOD = REQUEST_METHOD.POST

    override val url: String = DETAIL_URL

    override val params: Map<String, String>
        get() = hashMapOf("id" to id)

    companion object {
        private const val DETAIL_URL = "http://m.webtoon.daum.net/data/mobile/webtoon/viewer"
        private const val SHARE_URL = "http://m.webtoon.daum.net/m/webtoon/viewer/"
    }
}
