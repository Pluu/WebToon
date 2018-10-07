package com.pluu.support.daum

import com.pluu.kotlin.iterator
import com.pluu.support.impl.AbstractDetailApi
import com.pluu.support.impl.REQUEST_METHOD
import com.pluu.webtoon.di.NetworkUseCase
import com.pluu.webtoon.item.*
import org.json.JSONObject

/**
 * 다음 웹툰 상세 API
 * Created by pluu on 2017-04-22.
 */
class DaumDetailApi(
    networkUseCase: NetworkUseCase
) : AbstractDetailApi(networkUseCase) {
    private lateinit var id: String

    override fun parseDetail(episode: IEpisode): DetailResult {
        this.id = episode.episodeId

        val json = try {
            JSONObject(requestApi()).optJSONObject("data")
        } catch (e: Exception) {
            return DetailResult.ErrorResult(ERROR_TYPE.NOT_SUPPORT)
        }

        if (json.optJSONObject("webtoonEpisode")?.optInt("price", 0) ?: 0 > 0) {
            return DetailResult.ErrorResult(ERROR_TYPE.COIN_NEED)
        }

        val titleInfo = json.optJSONObject("webtoonEpisode")

        val ret = DetailResult.Detail(
            webtoonId = episode.webToonId,
            episodeId = titleInfo.optString("id")
        )
        ret.title = titleInfo.optString("title")

        ret.nextLink = json.optInt("nextEpisodeId", 0).takeIf {
            it > 0
        }?.toString()
        ret.prevLink = json.optInt("prevEpisodeId", 0).takeIf {
            it > 0
        }?.toString()

        ret.list = defaultDetailParse(json)

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

    override fun getDetailShare(episode: Episode, detail: DetailResult.Detail) = ShareItem(
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
