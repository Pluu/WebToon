package com.pluu.webtoon.data.remote.api

import com.pluu.webtoon.model.DetailResult
import com.pluu.webtoon.model.EpisodeId
import com.pluu.webtoon.model.EpisodeResult
import com.pluu.webtoon.model.Result
import com.pluu.webtoon.model.ShareItem
import com.pluu.webtoon.model.ToonId
import com.pluu.webtoon.model.ToonInfo
import com.pluu.webtoon.model.WeekPosition

internal interface WeeklyApi {

    val currentTabs: List<String>

    @Throws(Exception::class)
    suspend operator fun invoke(param: WeekPosition): Result<List<ToonInfo>>
}

internal interface EpisodeApi {
    suspend operator fun invoke(param: Param): Result<EpisodeResult>

    class Param(
        val toonId: ToonId,
        val page: Int
    )
}

internal interface DetailApi {
    suspend operator fun invoke(param: Param): DetailResult

    class Param(
        val toonId: ToonId,
        val episodeId: EpisodeId,
        var episodeTitle: String
    )
}

internal interface ShareApi {
    operator fun invoke(param: Param): ShareItem

    class Param(
        val toonId: ToonId,
        val episodeId: EpisodeId,
        val episodeTitle: String,
        val detailTitle: String,
    )
}