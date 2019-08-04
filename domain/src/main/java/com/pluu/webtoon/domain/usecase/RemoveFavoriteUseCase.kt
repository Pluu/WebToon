package com.pluu.webtoon.domain.usecase

import com.pluu.webtoon.NAV_ITEM
import com.pluu.webtoon.data.db.IDBHelper

/**
 * Delete Favorite Use Case
 */
class RemoveFavoriteUseCase(
    private val realmHelper: IDBHelper,
    private val naviItem: NAV_ITEM
) {
    suspend operator fun invoke(id: String) {
        realmHelper.removeFavorite(naviItem, id)
    }
}
