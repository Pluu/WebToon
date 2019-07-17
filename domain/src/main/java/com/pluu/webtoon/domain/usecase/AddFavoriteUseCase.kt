package com.pluu.webtoon.domain.usecase

import com.pluu.webtoon.NAV_ITEM
import com.pluu.webtoon.data.db.IDBHelper

/**
 * Add Favorite Use Case
 */
class AddFavoriteUseCase(
    private val realmHelper: IDBHelper,
    private val naviItem: NAV_ITEM
) {
    operator fun invoke(id: String) {
        realmHelper.addFavorite(naviItem, id)
    }
}
