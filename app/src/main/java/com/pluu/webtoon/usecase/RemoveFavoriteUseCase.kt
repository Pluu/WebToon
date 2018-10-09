package com.pluu.webtoon.usecase

import com.pluu.support.impl.NAV_ITEM
import com.pluu.webtoon.db.RealmHelper

/**
 * Delete Favorite Use Case
 */
class RemoveFavoriteUseCase(
    private val realmHelper: RealmHelper,
    private val naviItem: NAV_ITEM
) {
    operator fun invoke(id: String) {
        realmHelper.removeFavorite(naviItem, id)
    }
}
