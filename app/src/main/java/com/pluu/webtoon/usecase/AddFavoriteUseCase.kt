package com.pluu.webtoon.usecase

import com.pluu.support.impl.NAV_ITEM
import com.pluu.webtoon.db.RealmHelper

/**
 * Add Favorite Use Case
 */
class AddFavoriteUseCase(
    private val realmHelper: RealmHelper,
    private val naviItem: NAV_ITEM
) {
    operator fun invoke(id: String) {
        realmHelper.addFavorite(naviItem, id)
    }
}
