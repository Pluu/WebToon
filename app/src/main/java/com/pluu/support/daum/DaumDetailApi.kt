package com.pluu.support.daum

import android.content.Context
import com.pluu.kotlin.iterator
import com.pluu.support.impl.AbstractDetailApi
import com.pluu.support.impl.NetworkSupportApi
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
        } catch (e : Exception ) {
            return ret
        }

        if (json.optJSONObject("webtoonEpisode")?.optInt("price", 0) ?: 0 > 0) {
            ret.errorType = ERROR_TYPE.COIN_NEED
            return ret
        }

        ret.apply {
            val info = json.optJSONObject("webtoonEpisode").apply {
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

            type = when (info.optString("multiType")) {
                "chatting" -> {
                    list = chattingDetailParse(json)
                    DETAIL_TYPE.DAUM_CHATTING
                }
                "multi" -> {
                    list = multiDetailParse(json)
                    DETAIL_TYPE.DAUM_MULTI
                }
                else -> {
                    list = defaultDetailParse(json)
                    DETAIL_TYPE.DEFAULT
                }
            }
        }
        return ret
    }

    private fun multiDetailParse(json: JSONObject): List<DetailView> {
        val list = mutableListOf<DetailView>()

        json.optJSONArray("webtoonEpisodePages").iterator().forEach { it ->
            it.optJSONArray("webtoonEpisodePageMultimedias")?.iterator()?.forEach { multimedia ->
                when (multimedia.optString("multimediaType")) {
                    "image" -> list.add(DetailView.generate(VIEW_TYPE.MULTI_IMAGE,
                            multimedia.optJSONObject("image").optString("url")))
                    "gif" -> list.add(DetailView.generate(VIEW_TYPE.MULTI_GIF,
                            multimedia.optJSONObject("image").optString("url")))
                }
            }
        }
        if (list.isNotEmpty()) {
            list.add(0, DetailView.createChatEmpty())
            list.add(DetailView.createChatEmpty())
        }
        return list
    }

    private fun defaultDetailParse(json: JSONObject): List<DetailView> {
        val list = mutableListOf<DetailView>()
        json.optJSONArray("webtoonImages")?.iterator()?.forEach { it ->
            list.add(DetailView.createImage(it.optString("url")))
        }
        json.optJSONArray("webtoonEpisodePages")?.iterator()?.forEach { it ->
            list.add(DetailView.createImage(it
                    .optJSONArray("webtoonEpisodePageMultimedias")
                    .optJSONObject(0)
                    .optJSONObject("image")
                    .optString("url")))
        }
        return list
    }

    private fun chattingDetailParse(json: JSONObject): List<DetailView> {
        val list = mutableListOf<DetailView>()
        json.optJSONArray("webtoonEpisodeChattings")?.iterator()?.forEach { it ->
            when(it.optString("messageType")) {
                "notice" -> {
                    if (it.isNull("message")) {
                        list.add(DetailView.createChatNoticeImage(it.optJSONObject("image").optString("url")))
                    } else {
                        list.add(DetailView.createChatNotice(it.optString("message")))
                    }
                }
                "another" -> {
                    list.add(DetailView.createChatLeft(
                            it.optJSONObject("profileImage").optString("url"),
                            it.optString("profileName"),
                            it.optString("message"))
                    )
                }
                "own" -> {
                    list.add(DetailView.createChatRight(
                            it.optJSONObject("profileImage").optString("url"),
                            it.optString("profileName"),
                            it.optString("message"))
                    )
                }
            }
        }
        if (list.isNotEmpty()) {
            list.add(0, DetailView.createChatEmpty())
            list.add(DetailView.createChatEmpty())
        }
        return list
    }

    override fun getDetailShare(episode: Episode, detail: Detail) = ShareItem(
        title = "${episode.title} / ${detail.title}",
        url = "$SHARE_URL$detail.episodeId"
    )

    override val method: String
        get() = NetworkSupportApi.POST

    override val url: String
        get() = DETAIL_URL

    override val params: Map<String, String>
        get() = hashMapOf("id" to id)

    companion object {

        private val DETAIL_URL = "http://m.webtoon.daum.net/data/mobile/webtoon/viewer"
        private val SHARE_URL = "http://m.webtoon.daum.net/m/webtoon/viewer/"
    }
}
