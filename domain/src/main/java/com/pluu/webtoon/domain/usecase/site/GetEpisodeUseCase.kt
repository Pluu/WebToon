package com.pluu.webtoon.domain.usecase.site

import com.pluu.webtoon.data.repository.WebToonRepository
import com.pluu.webtoon.model.EpisodeResult
import com.pluu.webtoon.model.Result
import com.pluu.webtoon.model.ToonId
import javax.inject.Inject

class GetEpisodeUseCase @Inject constructor(
    private val repository: WebToonRepository
) {
    suspend operator fun invoke(toonId: ToonId, page: Int): Result<EpisodeResult> {
        return repository.getEpisodes(toonId, page)
    }
}