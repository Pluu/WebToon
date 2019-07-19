package com.pluu.webtoon.support.ktoon

import android.net.Uri
import com.pluu.core.Result
import com.pluu.core.asSequence
import com.pluu.webtoon.data.model.IRequest
import com.pluu.webtoon.data.model.REQUEST_METHOD
import com.pluu.webtoon.data.network.INetworkUseCase
import com.pluu.webtoon.data.network.mapDocument
import com.pluu.webtoon.data.network.mapJson
import com.pluu.webtoon.domain.base.AbstractDetailApi
import com.pluu.webtoon.domain.moel.DetailResult
import com.pluu.webtoon.domain.moel.DetailView
import com.pluu.webtoon.domain.moel.ERROR_TYPE
import com.pluu.webtoon.domain.usecase.param.DetailRequest
import org.json.JSONArray
import org.jsoup.nodes.Document

/**
 * 올레 웹툰 상세 API
 * Created by pluu on 2017-04-22.
 */
class KToonDetailApi(
    private val networkUseCase: INetworkUseCase
) : AbstractDetailApi, INetworkUseCase by networkUseCase {

    override suspend fun invoke(param: DetailRequest): DetailResult {
        val id = param.episodeId

        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val responseData = requestApi(createApi(id))
            .mapJson()
            .let { result ->
                when (result) {
                    is Result.Success -> result.data
                    is Result.Error -> return DetailResult.ErrorResult(ERROR_TYPE.DEFAULT_ERROR)
                }
            }

        ///////////////////////////////////////////////////////////////////////////
        // Parse Data
        ///////////////////////////////////////////////////////////////////////////
        val array: JSONArray = responseData
            .optJSONArray("response") ?: return DetailResult.ErrorResult(ERROR_TYPE.DEFAULT_ERROR)

        val ret = DetailResult.Detail(
            webtoonId = param.toonId,
            episodeId = param.episodeId
        ).apply {
            title = param.episodeTitle
        }
        ret.list = parserToon(array)
        parsePrevNext(id).let {
            ret.prevLink = it.first
            ret.nextLink = it.second
        }
        return ret
    }

    private fun parserToon(array: JSONArray): List<DetailView> {
        return array.asSequence()
            .map {
                DetailView(it.optString("imagepath"))
            }
            .toList()
    }

    private suspend fun parsePrevNext(id: String): Pair<String?, String?> {
        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val request = IRequest(
            url = Uri.Builder()
                .encodedPath("https://www.myktoon.com/mw/works/viewer.kt")
                .appendQueryParameter("timesseq", id)
                .build().toString()
        )

        val responseData: Document = requestApi(request)
            .mapDocument()
            .let { result ->
                when (result) {
                    is Result.Success -> result.data
                    is Result.Error -> return null to null
                }
            }

        ///////////////////////////////////////////////////////////////////////////
        // Parse Data
        ///////////////////////////////////////////////////////////////////////////

        val pagingWrap = responseData.select(".paging_wrap")
        return Pair(
            pagingWrap.select("a[class=btn_prev moveViewerBtn]").attr("data-seq"),
            pagingWrap.select("a[class=btn_next moveViewerBtn]").attr("data-seq")
        )
    }

    private fun createApi(id: String): IRequest =
        IRequest(
            method = REQUEST_METHOD.POST,
            url = "https://v2.myktoon.com/web/works/times_image_list_ajax.kt",
            params = mapOf(
                "timesseq" to id
            )
        )
}
