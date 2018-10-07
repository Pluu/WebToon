package com.pluu.webtoon.usecase

import com.pluu.support.impl.NAV_ITEM
import com.pluu.webtoon.db.RealmHelper
import com.pluu.webtoon.item.DetailResult

class ReadUseCase(
    private val realmHelper: RealmHelper,
    private val naviItem: NAV_ITEM
) {
    fun readEpisode(item: DetailResult.Detail) {
        realmHelper.readEpisode(naviItem, item.webtoonId, item.episodeId)
    }
}
