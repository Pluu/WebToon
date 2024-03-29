package com.pluu.webtoon.domain.usecase

import com.pluu.webtoon.data.repository.WebToonRepository
import com.pluu.webtoon.model.Episode
import com.pluu.webtoon.model.NAV_ITEM
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * EpisodeInfo Section Use Case
 */
class ReadEpisodeListUseCase @Inject constructor(
    private val repository: WebToonRepository
) {
    /**
     * 이미 읽은 EpisodeInfo 취득
     *
     * @param type Type
     * @param id EpisodeInfo ID
     * @return Read List
     */
    operator fun invoke(type: NAV_ITEM, id: String): Flow<List<Episode>> =
        repository.getReadEpisode(type.name, id)
}
