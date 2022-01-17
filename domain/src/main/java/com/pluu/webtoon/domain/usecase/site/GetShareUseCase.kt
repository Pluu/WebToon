package com.pluu.webtoon.domain.usecase.site

import com.pluu.webtoon.data.repository.WebToonRepository
import com.pluu.webtoon.model.EpisodeId
import com.pluu.webtoon.model.ShareItem
import com.pluu.webtoon.model.ToonId
import javax.inject.Inject

class GetShareUseCase @Inject constructor(
    private val repository: WebToonRepository
) {
    operator fun invoke(
        toonId: ToonId,
        episodeId: EpisodeId,
        episodeTitle: String,
        detailTitle: String
    ): ShareItem {
        return repository.getShareItem(toonId, episodeId, episodeTitle, detailTitle)
    }
}