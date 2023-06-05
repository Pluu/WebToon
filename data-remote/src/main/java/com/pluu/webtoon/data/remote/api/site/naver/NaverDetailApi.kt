package com.pluu.webtoon.data.remote.api.site.naver

import com.pluu.utils.mapEach
import com.pluu.webtoon.data.remote.api.DetailApi
import com.pluu.webtoon.data.remote.model.IRequest
import com.pluu.webtoon.data.remote.network.INetworkUseCase
import com.pluu.webtoon.data.remote.utils.mapDocument
import com.pluu.webtoon.data.remote.utils.mapJson
import com.pluu.webtoon.model.DetailResult
import com.pluu.webtoon.model.DetailView
import com.pluu.webtoon.model.ERROR_TYPE
import com.pluu.webtoon.model.Result
import com.pluu.webtoon.model.successOr
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.json.JSONObject
import org.jsoup.nodes.Document
import javax.inject.Inject

/**
 * 네이버 웹툰 상세 API
 * Created by pluu on 2017-04-20.
 */
internal class NaverDetailApi @Inject constructor(
    private val networkUseCase: INetworkUseCase
) : DetailApi, INetworkUseCase by networkUseCase {

    @Suppress("PrivatePropertyName")
    private val SKIP_DETAIL = arrayOf(
        "http://static.naver.com/m/comic/im/txt_ads.png",
        "http://static.naver.com/m/comic/im/toon_app_pop.png"
    )

    private val json by lazy {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }

    private val articleWordRegex by lazy {
        "(?<='?)\\w+(?='?)".toRegex()
    }
    private val articleNodRegex by lazy {
        "(?<='?)\\d+(?='?)".toRegex()
    }

    override suspend fun invoke(param: DetailApi.Param): DetailResult {
        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val responseData: Document = requestApi(createApi(param.toonId, param.episodeId))
            .mapDocument()
            .let { result ->
                when (result) {
                    is Result.Success -> result.data
                    is Result.Error -> return DetailResult.ErrorResult(
                        ERROR_TYPE.DEFAULT_ERROR(result.throwable)
                    )
                }
            }

        ///////////////////////////////////////////////////////////////////////////
        // Parse Data
        ///////////////////////////////////////////////////////////////////////////
        val ret = DetailResult.Detail(
            webtoonId = param.toonId,
            episodeId = param.episodeId,
            title = responseData.select("div[class=chh] span, h1[class=tit]").first()?.text().orEmpty()
        )

        val parser = when {
            // osLoader
            responseData.select("#ct > .oz-loader").isNotEmpty() -> ::parseOsLoader
            // Fixed
            responseData.select("#ct > .toon_view_lst").isNotEmpty() -> ::parseFixed
            // 컷툰
            responseData.select("div[class=viewer cuttoon]").isNotEmpty() -> ::parseCutToon
            // 일반 웹툰
            else -> ::parseNormal
        }

        return runCatching {
            when (val result = parser(ret, responseData)) {
                is TypeResult.Success -> ret.apply {
                    list = result.list
                    prevLink = result.prev
                    nextLink = result.next
                }
                TypeResult.NotSupport -> {
                    DetailResult.ErrorResult(ERROR_TYPE.NOT_SUPPORT)
                }
            }
        }.getOrElse {
            DetailResult.ErrorResult(ERROR_TYPE.DEFAULT_ERROR(it))
        }
    }

    @Suppress("RedundantSuspendModifier")
    private suspend fun parseNormal(ret: DetailResult.Detail, doc: Document): TypeResult {
        val list = parseDetailNormalType(doc)

        if (list.isEmpty()) {
            return TypeResult.NotSupport
        }

        // 이전, 다음화
        val (prev, next) = doc.select(".paging_wrap").let { wrap ->
            wrap.select("[data-type=next]").takeIf { it.isNotEmpty() }?.let {
                (ret.episodeId.toInt() + 1).toString()
            } to wrap.select("[data-type=prev]").takeIf { it.isNotEmpty() }?.let {
                (ret.episodeId.toInt() - 1).toString()
            }
        }

        return TypeResult.Success(
            list = list,
            prev = prev,
            next = next
        )
    }

    private fun parseDetailNormalType(doc: Document) = doc.select("#toonLayer li img")
        .map {
            it.attr("data-original").takeIf { url ->
                url.isNotEmpty()
            } ?: it.attr("src")
        }
        .filter { it.isNotEmpty() && !SKIP_DETAIL.contains(it) }
        .map { DetailView(it) }

    @Suppress("RedundantSuspendModifier", "UNUSED_PARAMETER")
    private suspend fun parseFixed(ret: DetailResult.Detail, doc: Document): TypeResult {
        val list = parseDetailFixedType(doc)

        if (list.isEmpty()) {
            return TypeResult.NotSupport
        }

        val (prev, next) = doc.select("script")
            .map { it.outerHtml() }
            .find { it.contains("prevArticle") }
            ?.replace("(\\s|\\n|\\t)".toRegex(), "")
            ?.let {
                parseFixedPage(it, "prevArticle") to parseFixedPage(it, "nextArticle")
            } ?: (null to null)

        return TypeResult.Success(
            list = list,
            prev = prev,
            next = next
        )
    }

    private fun parseFixedPage(src: String, key: String): String? {
        return src.indexOf(key)
            .takeIf { it > -1 }
            ?.let { start ->
                val openIndex = src.indexOf("{", startIndex = start)
                val closeIndex = src.indexOf("}", startIndex = start)
                // 간혹 JSON의 예약어가 들어올 경우 변경 대응
                val article = json.decodeFromString<Article>(
                    src.substring(openIndex, closeIndex + 1).replace("'", "\"")
                )
                if (articleWordRegex.find(article.chargeYn)?.value == "Y") {
                    // 과금 에피소드인 경우, 이동 불가능 처리
                    return null
                }
                articleNodRegex.find(article.no)?.value
            }
    }

    private fun parseDetailFixedType(doc: Document) = doc.select("#ct ul li img")
        .map { it.attr("data-src") }
        .map { url -> DetailView(url) }

    @Suppress("RedundantSuspendModifier", "UNUSED_PARAMETER")
    private suspend fun parseCutToon(ret: DetailResult.Detail, doc: Document): TypeResult {
        val list = parseDetailCutToonType(doc)

        if (list.isEmpty()) {
            return TypeResult.NotSupport
        }

        val (prev, next) = doc.select("script")
            .find { it.outerHtml().contains("prevArticle") }
            ?.let {
                val html = it.outerHtml().replace("(\\s+\\n+\\t+)".toRegex(), "")
                val match = "(?<=no: ')\\d*(?=')".toRegex().find(html)
                match?.value to match?.next()?.value
            } ?: (null to null)

        return TypeResult.Success(
            list = list,
            prev = prev,
            next = next
        )
    }

    private fun parseDetailCutToonType(doc: Document) = doc.select("#ct .swiper-slide > img")
        .map { it.attr("data-src") }
        .map { url -> DetailView(url) }

    @Suppress("UNUSED_PARAMETER")
    private suspend fun parseOsLoader(ret: DetailResult.Detail, doc: Document): TypeResult {
        val infoScript = doc.getElementsByTag("script")
            .firstOrNull {
                it.html().contains("effecttoonContent")
            }?.html() ?: return TypeResult.NotSupport

        val imageUrl = "(?<=imageUrl: ').*(?=')".toRegex().find(infoScript)?.value
        val motionDataUrl = "(?<=documentUrl: ').*(?=')".toRegex().find(infoScript)?.value

        if (imageUrl.isNullOrEmpty() || motionDataUrl.isNullOrEmpty()) return TypeResult.NotSupport

        val list = getMoreResponse(motionDataUrl)
            ?.optJSONObject("assets")
            ?.optJSONObject("stillcut")
            ?.mapEach {
                DetailView("${imageUrl}/$it")
            } ?: return TypeResult.NotSupport

        if (list.isEmpty()) {
            return TypeResult.NotSupport
        }

        val articleNoRegex = "(?<=no: ').*(?=')".toRegex()
        fun findArticleNo(keyword: String): String? = infoScript.indexOf(keyword).takeIf { it > -1 }
            ?.let { startIndex ->
                articleNoRegex.find(infoScript, startIndex)?.value
            }

        return TypeResult.Success(
            list = list,
            prev = findArticleNo("prevArticle"),
            next = findArticleNo("nextArticle")
        )
    }

    private fun createApi(toonId: String, episodeId: String): IRequest =
        IRequest(
            url = detailCreate(
                toonId,
                episodeId
            )
        )

    private suspend fun getMoreResponse(
        url: String
    ): JSONObject? {
        val request = IRequest(url = url)
        return requestApi(request)
            .mapJson()
            .successOr(null)
    }

    companion object {
        val detailCreate = { toonId: String, episodeId: String ->
            "http://m.comic.naver.com/webtoon/detail.nhn?titleId=$toonId&no=$episodeId"
        }
    }

}

private sealed class TypeResult {
    class Success(
        val list: List<DetailView>,
        val prev: String? = null,
        val next: String? = null
    ) : TypeResult()

    object NotSupport : TypeResult()
}

@Serializable
data class Article(
    val no: String,
    val chargeYn: String
)