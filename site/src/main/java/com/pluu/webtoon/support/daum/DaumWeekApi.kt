package com.pluu.webtoon.support.daum

import com.pluu.utils.asSequence
import com.pluu.webtoon.data.model.IRequest
import com.pluu.webtoon.data.network.INetworkUseCase
import com.pluu.webtoon.data.network.mapJson
import com.pluu.webtoon.domain.usecase.WeeklyUseCase
import com.pluu.webtoon.domain.usecase.param.WeeklyRequest
import com.pluu.webtoon.model.Result
import com.pluu.webtoon.model.Status
import com.pluu.webtoon.model.ToonInfo
import org.json.JSONObject

/**
 * 카카오 웹툰 Week Api
 * Created by pluu on 2017-04-20.
 */
internal class DaumWeekApi(
    private val networkUseCase: INetworkUseCase
) : WeeklyUseCase, INetworkUseCase by networkUseCase {

    override val currentTabs = arrayOf("월", "화", "수", "목", "금", "토", "일", "완결")

    override suspend fun invoke(param: WeeklyRequest): Result<List<ToonInfo>> {
        ///////////////////////////////////////////////////////////////////////////
        // API
        ///////////////////////////////////////////////////////////////////////////

        val responseData = requestApi(createApi())
            .mapJson()
            .let { result ->
                when (result) {
                    is Result.Success -> result.data
                    is Result.Error -> return Result.Error(result.throwable)
                }
            }

        ///////////////////////////////////////////////////////////////////////////
        // Parse Data
        ///////////////////////////////////////////////////////////////////////////

        val cards = responseData
            .optJSONObject("data")
            ?.optJSONArray("sections")
            ?.asSequence()
            ?.firstOrNull {
                it.optString("title") == currentTabs[param]
            }?.optJSONArray("cardGroups")
            ?.asSequence()
            ?.first()
            ?.optJSONArray("cards") ?: return Result.Success(emptyList())

        val list = cards
            .asSequence()
            .map { createToon(it) }
            .toList()
        return Result.Success(list)
    }

    private fun createToon(
        obj: JSONObject
    ): ToonInfo {
        val content = obj.getJSONObject("content")
        return ToonInfo(
            id = content.optString("id"),
            title = content.optString("title"),
            image = content.getThumbnail(),
            backgroundColor = content.getString("backgroundColor"),
            writer = content.optJSONArray("authors")
                ?.asSequence().orEmpty()
                .filterNot {
                    it.optString("type") == "PUBLISHER"
                }.map {
                    it.optString("name")
                }.joinToString(),
            updateDate = content.getString("lastEpisodePublicationDateTime")
                .substring(0, 10)
                .replace("-", "."),
            status = when {
                content.optBoolean("upContent") -> Status.UPDATE // 최근 업데이트
                content.optBoolean("rest") -> Status.BREAK // 휴재
                else -> Status.NONE
            },
            isAdult = content.optBoolean("adult")
        )
    }

    private fun JSONObject.getThumbnail(): String {
        val anchor = optJSONObject("anchorClip")
        if (anchor?.isNull("clipFirstFrame") == false) {
            anchor.getString("clipFirstFrame") + ".w.png"
        }

        return getString("featuredCharacterImageB") + ".png"
    }

    private fun createApi(): IRequest =
        IRequest(
            url = "https://gateway-kw.kakao.com/section/v1/pages/general-weekdays"
        )
}
