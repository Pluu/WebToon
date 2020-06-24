package com.pluu.webtoon.support.naver

import com.pluu.core.mapEach
import com.pluu.webtoon.data.model.IRequest
import com.pluu.webtoon.data.network.INetworkUseCase
import com.pluu.webtoon.data.network.mapJson
import com.pluu.webtoon.domain.model.DetailResult
import com.pluu.webtoon.domain.model.DetailView
import com.pluu.webtoon.domain.model.ERROR_TYPE
import com.pluu.webtoon.domain.usecase.DetailUseCase
import com.pluu.webtoon.domain.usecase.param.DetailRequest
import com.pluu.webtoon.model.Result
import com.pluu.webtoon.network.mapDocument
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.json.JSONObject
import org.jsoup.nodes.Document

/**
 * 네이버 웹툰 상세 API
 * Created by pluu on 2017-04-20.
 */
internal class NaverDetailApi(
    private val networkUseCase: INetworkUseCase
) : DetailUseCase, INetworkUseCase by networkUseCase {

    private val SKIP_DETAIL = arrayOf(
        "http://static.naver.com/m/comic/im/txt_ads.png",
        "http://static.naver.com/m/comic/im/toon_app_pop.png"
    )

    private val json by lazy {
        Json(JsonConfiguration(ignoreUnknownKeys = true, isLenient = true))
    }

    private val articleWordRegex by lazy {
        "(?<='?)\\w+(?='?)".toRegex()
    }
    private val articleNodRegex by lazy {
        "(?<='?)\\d+(?='?)".toRegex()
    }

    override suspend fun invoke(param: DetailRequest): DetailResult {
        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val responseData: Document = requestApi(createApi(param.toonId, param.episodeId))
            .mapDocument()
            .let { result ->
                when (result) {
                    is Result.Success -> result.data
                    is Result.Error -> return DetailResult.ErrorResult(
                        ERROR_TYPE.DEFAULT_ERROR(result.exception)
                    )
                }
            }

        ///////////////////////////////////////////////////////////////////////////
        // Parse Data
        ///////////////////////////////////////////////////////////////////////////
        val ret = DetailResult.Detail(
            webtoonId = param.toonId,
            episodeId = param.episodeId
        )
        ret.title = responseData.select("div[class=chh] span, h1[class=tit]").first()?.text()

        val parser = when {
            // osLoader
            responseData.select("#ct > .oz-loader")?.isNotEmpty() == true -> ::parseOsLoader
            // Fixed
            responseData.select("#ct > .toon_view_lst")?.isNotEmpty() == true -> ::parseFixed
            // 컷툰
            responseData.select("div[class=viewer cuttoon]")
                ?.isNotEmpty() == true -> ::parseCutToon
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

    private suspend fun parseNormal(ret: DetailResult.Detail, doc: Document): TypeResult {
        val list = parseDetailNormalType(doc)

        if (list.isEmpty()) {
            return TypeResult.NotSupport
        }

        // 이전, 다음화
        val (prev, next) = doc.select(".paging_wrap")?.let { wrap ->
            wrap.select("[data-type=next]").takeIf { it.isNotEmpty() }?.let {
                (ret.episodeId.toInt() + 1).toString()
            } to wrap.select("[data-type=prev]").takeIf { it.isNotEmpty() }?.let {
                (ret.episodeId.toInt() - 1).toString()
            }
        } ?: (null to null)

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
                val article = json.parse(
                    Article.serializer(),
                    src.substring(openIndex, closeIndex + 1)
                )
                if (articleWordRegex.find(article.chargeYn)?.value == "Y") {
                    return null
                }
                articleNodRegex.find(article.no)?.value
            }
    }

    private fun parseDetailFixedType(doc: Document) = doc.select("#ct ul li img")
        .map { it.attr("data-src") }
        .map { url -> DetailView(url) }

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
            .let { result ->
                when (result) {
                    is Result.Success -> result.data
                    is Result.Error -> null
                }
            }
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