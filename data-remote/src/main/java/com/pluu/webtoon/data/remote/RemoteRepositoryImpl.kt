package com.pluu.webtoon.data.remote

import com.pluu.webtoon.data.remote.api.DetailApi
import com.pluu.webtoon.data.remote.api.EpisodeApi
import com.pluu.webtoon.data.remote.api.ShareApi
import com.pluu.webtoon.data.remote.api.WeeklyApi
import com.pluu.webtoon.data.repository.RemoteRepository
import com.pluu.webtoon.model.DetailResult
import com.pluu.webtoon.model.EpisodeId
import com.pluu.webtoon.model.EpisodeResult
import com.pluu.webtoon.model.Result
import com.pluu.webtoon.model.ShareItem
import com.pluu.webtoon.model.ToonId
import com.pluu.webtoon.model.ToonInfo
import com.pluu.webtoon.model.WeekPosition
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(
    private val weeklyApi: WeeklyApi,
    private val episodeApi: EpisodeApi,
    private val detailApi: DetailApi,
    private val shareApi: ShareApi
) : RemoteRepository {
    override suspend fun getWeekly(weekPosition: WeekPosition): Result<List<ToonInfo>> {
        return weeklyApi.invoke(weekPosition)
    }

    override fun getWebToonTabs(): Array<String> {
        return weeklyApi.currentTabs
    }

    override suspend fun getEpisodes(toonId: String, page: Int): Result<EpisodeResult> {
        return episodeApi(EpisodeApi.Param(toonId, page))
    }

    override suspend fun getDetail(
        toonId: String,
        episodeId: String,
        episodeTitle: String
    ): DetailResult {
        return detailApi(DetailApi.Param(toonId, episodeId, episodeTitle))
    }

    override fun getShareItem(
        toonId: ToonId,
        episodeId: EpisodeId,
        episodeTitle: String,
        detailTitle: String,
    ): ShareItem {
        return shareApi(ShareApi.Param(toonId, episodeId, episodeTitle, detailTitle))
    }
}