package com.pluu.webtoon.data.ktoon

import android.net.Uri
import com.pluu.kotlin.asSequence
import com.pluu.webtoon.data.DetailRequest
import com.pluu.webtoon.data.IRequest
import com.pluu.webtoon.data.REQUEST_METHOD
import com.pluu.webtoon.data.impl.AbstractDetailApi
import com.pluu.webtoon.di.INetworkUseCase
import com.pluu.webtoon.item.DetailResult
import com.pluu.webtoon.item.DetailView
import com.pluu.webtoon.item.ERROR_TYPE
import com.pluu.webtoon.item.Result
import com.pluu.webtoon.utils.safeApi
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup

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

        val responseData = requestApi(createApi(id)).safeApi { response ->
            JSONObject(response)
        }.let { result ->
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

        val responseData = requestApi(request).safeApi { response ->
            Jsoup.parse(response)
        }.let { result ->
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

    private fun createApi(id: String): IRequest = IRequest(
        method = REQUEST_METHOD.POST,
        url = "https://v2.myktoon.com/web/works/times_image_list_ajax.kt",
        params = mapOf(
            "timesseq" to id
        )
    )
}
