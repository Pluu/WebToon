package com.pluu.webtoon.domain.usecase

import com.pluu.webtoon.NAV_ITEM
import com.pluu.webtoon.data.db.IDBHelper
import com.pluu.webtoon.data.model.DBToon

/**
 * Add Favorite Use Case
 */
class AddFavoriteUseCase(
    private val dbHelper: IDBHelper
) {
    suspend operator fun invoke(type: NAV_ITEM, id: String) {
        dbHelper.addFavorite(
            DBToon(
                id = 0,
                service = type.name,
                toonId = id
            )
        )
    }
}
