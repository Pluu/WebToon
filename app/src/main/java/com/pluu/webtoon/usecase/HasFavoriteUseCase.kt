package com.pluu.webtoon.usecase

import com.pluu.support.impl.NAV_ITEM
import com.pluu.webtoon.db.RealmHelper

/**
 * 즐겨찾기 여부 UseCase
 */
class HasFavoriteUseCase(
    private val realmHelper: RealmHelper,
    private val naviItem: NAV_ITEM
) {
    /**
     * 즐겨찾기 여부 판단
     * @param id 웹툰 ID
     * @return true/false
     */
    operator fun invoke(id: String): Boolean =
        realmHelper.isFavorite(naviItem, id)
}
