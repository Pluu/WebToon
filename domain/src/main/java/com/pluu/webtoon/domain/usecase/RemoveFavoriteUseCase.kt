package com.pluu.webtoon.domain.usecase

import com.pluu.webtoon.NAV_ITEM
import com.pluu.webtoon.data.dao.IDBHelper
import com.pluu.webtoon.data.model.DBToon

/**
 * Delete Favorite Use Case
 */
class RemoveFavoriteUseCase(
    private val dbHelper: IDBHelper
) {
    suspend operator fun invoke(type: NAV_ITEM, id: String) {
        dbHelper.removeFavorite(
            DBToon(
                id = 0,
                service = type.name,
                toonId = id
            )
        )
    }
}
