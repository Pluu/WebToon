package com.pluu.webtoon.data.repository

import com.pluu.webtoon.model.DetailResult
import com.pluu.webtoon.model.Episode
import com.pluu.webtoon.model.EpisodeId
import com.pluu.webtoon.model.EpisodeResult
import com.pluu.webtoon.model.Result
import com.pluu.webtoon.model.ShareItem
import com.pluu.webtoon.model.Toon
import com.pluu.webtoon.model.ToonId
import com.pluu.webtoon.model.ToonInfo
import com.pluu.webtoon.model.WeekPosition
import kotlinx.coroutines.flow.Flow

interface WebToonRepository {
    suspend fun isFavorite(serviceName: String, id: String): Boolean

    fun getReadEpisode(serviceName: String, id: String): Flow<List<Episode>>

    suspend fun addFavorite(item: Toon)

    suspend fun removeFavorite(item: Toon)

    suspend fun readEpisode(item: Episode)

    suspend fun getWeekly(weekPosition: WeekPosition): Result<List<ToonInfo>>

    suspend fun getEpisodes(toonId: String, page: Int): Result<EpisodeResult>

    suspend fun getDetail(
        toonId: ToonId,
        episodeId: EpisodeId,
        episodeTitle: String
    ): DetailResult

    fun getShareItem(
        toonId: ToonId,
        episodeId: EpisodeId,
        episodeTitle: String,
        detailTitle: String,
    ): ShareItem

    fun getWebToonTabs(): Array<String>
}