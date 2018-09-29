package com.pluu.support.kakao

import com.pluu.kotlin.asSequence
import com.pluu.support.impl.AbstractDetailApi
import com.pluu.support.impl.REQUEST_METHOD
import com.pluu.webtoon.di.NetworkUseCase
import com.pluu.webtoon.item.Detail
import com.pluu.webtoon.item.DetailView
import com.pluu.webtoon.item.Episode
import com.pluu.webtoon.item.ShareItem
import com.pluu.webtoon.utils.buildRequest
import com.pluu.webtoon.utils.toFormBody
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.withContext
import org.json.JSONObject

/**
 * 카카오 페이지 웹툰 상세 API
 * Created by pluu on 2017-04-25.
 */
class KakaoDetailApi(
    networkUseCase: NetworkUseCase
) : AbstractDetailApi(networkUseCase) {

    private val DETAIL_URL =
        "http://page.kakao.com/viewer?productId=%s&categoryUid=10&subCategoryUid=0"
    private lateinit var id: String

    override fun parseDetail(episode: Episode): Detail {
        this.id = episode.episodeId

        return runBlocking {
            val json: JSONObject? = withContext(Dispatchers.Default) {
                getData()
            }
            val prev: String? = withContext(Dispatchers.Default) {
                getMoreData(episode.toonId, id, isPrev = true)
            }
            val next = withContext(Dispatchers.Default) {
                getMoreData(episode.toonId, id, isPrev = false)
            }

            Detail().apply {
                webtoonId = episode.toonId
                episodeId = id
                title = episode.title
                list = json?.let { getImages(it) }
                prevLink = prev
                nextLink = next
            }
        }
    }

    @Throws
    private fun getData(): JSONObject? = requestApi().takeIf { it.isNotEmpty() }?.let {
        JSONObject(it)
    }

    private fun getImages(json: JSONObject): List<DetailView>? {
        val memberInfo: JSONObject? =
            json.optJSONObject("downloadData")?.optJSONObject("members")
        return memberInfo?.optString("sAtsServerUrl")?.takeIf { it.isNotEmpty() }?.let { host ->
            memberInfo.optJSONArray("files")
                ?.asSequence()
                ?.map {
                    DetailView("$host${it.optString("secureUrl")}")
                }?.toList()
        }
    }

    private fun getMoreData(toonId: String, episodeId: String, isPrev: Boolean): String? {
        val request = buildRequest {
            post(
                mapOf(
                    "seriesPid" to toonId,
                    "singlePid" to episodeId
                ).toFormBody()
            )
            url(
                if (isPrev) "https://api2-page.kakao.com/api/v5/inven/get_prev_item"
                else "https://api2-page.kakao.com/api/v5/inven/get_next_item"
            )
        }
        return JSONObject(requestApi(request))
            .optJSONObject("item")?.takeIf { it.optString("hidden") == "N" }
            ?.optString("pid")
            ?.replace("[^\\d]".toRegex(), "")
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
