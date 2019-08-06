package com.pluu.webtoon.domain.usecase

import com.pluu.webtoon.NAV_ITEM
import com.pluu.webtoon.data.db.IDBHelper
import com.pluu.webtoon.data.model.DBToon

/**
 * Delete Favorite Use Case
 */
class RemoveFavoriteUseCase(
    private val dbHelper: IDBHelper,
    private val naviItem: NAV_ITEM
) {
    suspend operator fun invoke(id: String) {
        dbHelper.removeFavorite(
            DBToon(
                id = 0,
                service = naviItem.name,
                toonId = id
            )
        )
    }
}
