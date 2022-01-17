package com.pluu.webtoon.data.remote.api.site.ktoon

import android.net.Uri
import com.pluu.utils.asSequence
import com.pluu.webtoon.data.remote.api.DetailApi
import com.pluu.webtoon.data.remote.model.IRequest
import com.pluu.webtoon.data.remote.network.INetworkUseCase
import com.pluu.webtoon.data.remote.utils.mapDocument
import com.pluu.webtoon.data.remote.utils.mapJson
import com.pluu.webtoon.model.DetailResult
import com.pluu.webtoon.model.DetailView
import com.pluu.webtoon.model.ERROR_TYPE
import com.pluu.webtoon.model.Result
import org.json.JSONArray
import org.jsoup.nodes.Document
import javax.inject.Inject

/**
 * 올레 웹툰 상세 API
 * Created by pluu on 2017-04-22.
 */
internal class KToonDetailApi @Inject constructor(
    private val networkUseCase: INetworkUseCase
) : DetailApi, INetworkUseCase by networkUseCase {

    override suspend fun invoke(param: DetailApi.Param): DetailResult {
        val id = param.episodeId

        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val responseData = requestApi(createApi(id))
            .mapJson()
            .let { result ->
                when (result) {
                    is Result.Success -> result.data
                    is Result.Error -> return DetailResult.ErrorResult(ERROR_TYPE.DEFAULT_ERROR(result.throwable))
                }
            }

        ///////////////////////////////////////////////////////////////////////////
        // Parse Data
        ///////////////////////////////////////////////////////////////////////////
        val array: JSONArray = responseData
            .optJSONArray("response") ?: return DetailResult.ErrorResult(ERROR_TYPE.DEFAULT_ERROR())

        val ret = DetailResult.Detail(
            webtoonId = param.toonId,
            episodeId = param.episodeId,
            title = param.episodeTitle
        )
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
            }.toList()
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
                    else -> return null to null
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
            method = com.pluu.webtoon.data.remote.model.REQUEST_METHOD.POST,
            url = "https://v2.myktoon.com/web/works/times_image_list_ajax.kt",
            params = mapOf(
                "timesseq" to id
            )
        )
}
