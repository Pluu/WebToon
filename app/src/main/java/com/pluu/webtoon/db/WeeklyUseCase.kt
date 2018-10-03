package com.pluu.webtoon.db

import com.pluu.support.impl.NAV_ITEM

/**
 * 주간 UseCase
 */
class WeeklyUseCase(
    private val realmHelper: RealmHelper,
    private val naviItem: NAV_ITEM
) {
    /**
     * 즐겨찾기 여부 판단
     * @param id 웹툰 ID
     * @return true/false
     */
    fun hasFavorite(id: String): Boolean =
        realmHelper.isFavorite(naviItem, id)
}
