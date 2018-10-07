package com.pluu.webtoon.usecase

import com.pluu.support.impl.NAV_ITEM
import com.pluu.webtoon.db.RealmHelper

/**
 * Favorite Use Case
 */
class FavoriteUseCase(
    private val realmHelper: RealmHelper,
    private val naviItem: NAV_ITEM
) {

    fun addFavorite(id: String) {
        realmHelper.addFavorite(naviItem, id)
    }

    fun removeFavorite(id: String) {
        realmHelper.removeFavorite(naviItem, id)
    }
}
