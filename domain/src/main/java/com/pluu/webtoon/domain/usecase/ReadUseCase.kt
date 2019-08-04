package com.pluu.webtoon.domain.usecase

import com.pluu.webtoon.NAV_ITEM
import com.pluu.webtoon.data.db.IDBHelper
import com.pluu.webtoon.domain.moel.DetailResult

class ReadUseCase(
    private val realmHelper: IDBHelper,
    private val naviItem: NAV_ITEM
) {
    suspend operator fun invoke(item: DetailResult.Detail) {
        realmHelper.readEpisode(naviItem, item.webtoonId, item.episodeId)
    }
}
