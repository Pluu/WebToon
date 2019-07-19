package com.pluu.webtoon.support.naver

import com.pluu.core.Result
import com.pluu.webtoon.data.model.IRequest
import com.pluu.webtoon.data.network.DetailRequest
import com.pluu.webtoon.data.network.INetworkUseCase
import com.pluu.webtoon.data.network.mapDocument
import com.pluu.webtoon.domain.moel.DetailResult
import com.pluu.webtoon.domain.moel.DetailView
import com.pluu.webtoon.domain.moel.ERROR_TYPE
import com.pluu.webtoon.support.impl.AbstractDetailApi
import org.jsoup.nodes.Document

/**
 * 네이버 웹툰 상세 API
 * Created by pluu on 2017-04-20.
 */
class NaverDetailApi(
    private val networkUseCase: INetworkUseCase
) : AbstractDetailApi, INetworkUseCase by networkUseCase {

    private val SKIP_DETAIL = arrayOf(
        "http://static.naver.com/m/comic/im/txt_ads.png",
        "http://static.naver.com/m/comic/im/toon_app_pop.png"
    )

    override suspend fun invoke(param: DetailRequest): DetailResult {
        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val responseData: Document = requestApi(createApi(param.toonId, param.episodeId))
            .mapDocument()
            .let { result ->
                when (result) {
                    is Result.Success -> result.data
                    is Result.Error -> return DetailResult.ErrorResult(ERROR_TYPE.DEFAULT_ERROR)
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

        when {
            responseData.select("#ct")?.isNotEmpty() == true -> {
                // 컷툰
                parseCutToon(ret, responseData)
            }
            responseData.select(".oz-loader")?.isNotEmpty() == true -> {
                // osLoader
                return DetailResult.ErrorResult(ERROR_TYPE.NOT_SUPPORT)
            }
            // 일반 웹툰
            else -> parseNormal(ret, responseData)
        }
        return ret
    }

    private fun parseNormal(ret: DetailResult.Detail, doc: Document) {
        ret.list = parseDetailNormalType(doc)

        // 이전, 다음화
        doc.select(".paging_wrap").apply {
            select("[data-type=next]").takeIf { it.isNotEmpty() }.let {
                ret.nextLink = (ret.episodeId.toInt() + 1).toString()
            }
            select("[data-type=prev]").takeIf { it.isNotEmpty() }.let {
                ret.prevLink = (ret.episodeId.toInt() - 1).toString()
            }
        }
    }

    private fun parseCutToon(ret: DetailResult.Detail, doc: Document) {
        ret.list = parseDetailCutToonType(doc)

        // 이전, 다음화
//        doc.select(".paging_wrap")?.apply {
//            select("[data-type=next]")?.attr("data-no")?.apply {
//                ret.nextLink = if (isNotEmpty()) this else null
//            }
//            select("[data-type=prev]")?.attr("data-no")?.apply {
//                ret.prevLink = if (isNotEmpty()) this else null
//            }
//        }
    }

    private fun parseDetailCutToonType(doc: Document): List<DetailView> {
        return doc.select("#ct ul li img")
            .map { it.attr("data-src") }
            .map { url -> DetailView(url) }
    }

    private fun parseDetailNormalType(doc: Document) =
        doc.select("#toonLayer li img")
            .map {
                it.attr("data-original").takeIf { url ->
                    url.isNotEmpty()
                } ?: it.attr("src")
            }
            .filter { it.isNotEmpty() && !SKIP_DETAIL.contains(it) }
            .map { DetailView(it) }

    private fun createApi(toonId: String, episodeId: String): IRequest =
        IRequest(
            url = detailCreate(
                toonId,
                episodeId
            )
        )

    companion object {
        val detailCreate = { toonId: String, episodeId: String ->
            "http://m.comic.naver.com/webtoon/detail.nhn?titleId=$toonId&no=$episodeId"
        }
    }

}
