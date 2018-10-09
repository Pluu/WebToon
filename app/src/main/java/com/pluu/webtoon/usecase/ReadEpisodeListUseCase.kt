package com.pluu.webtoon.usecase

import com.pluu.support.impl.NAV_ITEM
import com.pluu.webtoon.db.RealmHelper
import com.pluu.webtoon.model.REpisode

/**
 * EpisodeInfo Section Use Case
 */
class ReadEpisodeListUseCase(
    private val realmHelper: RealmHelper,
    private val naviItem: NAV_ITEM
) {
    /**
     * 이미 읽은 EpisodeInfo 취득
     * @param id EpisodeInfo ID
     * @return Read List
     */
    operator fun invoke(id: String): List<REpisode> =
        realmHelper.getEpisode(naviItem, id)
}
