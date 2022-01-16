package com.pluu.webtoon.domain.usecase

import com.pluu.webtoon.data.repository.WebToonRepository
import com.pluu.webtoon.model.DetailResult
import com.pluu.webtoon.model.Episode
import com.pluu.webtoon.model.NAV_ITEM
import javax.inject.Inject

class ReadUseCase @Inject constructor(
    private val repository: WebToonRepository
) {
    suspend operator fun invoke(type: NAV_ITEM, item: DetailResult.Detail) {
        repository.readEpisode(
            Episode(
                service = type.name,
                toonId = item.webtoonId,
                episodeId = item.episodeId
            )
        )
    }
}
