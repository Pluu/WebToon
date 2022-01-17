package com.pluu.webtoon.domain.usecase.site

import com.pluu.webtoon.data.repository.WebToonRepository
import com.pluu.webtoon.model.DetailResult
import com.pluu.webtoon.model.EpisodeId
import com.pluu.webtoon.model.ToonId
import javax.inject.Inject

class GetDetailUseCase @Inject constructor(
    private val repository: WebToonRepository
) {
    suspend operator fun invoke(
        toonId: ToonId,
        episodeId: EpisodeId,
        episodeTitle: String
    ): DetailResult {
        return repository.getDetail(toonId, episodeId, episodeTitle)
    }
}