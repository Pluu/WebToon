package com.pluu.webtoon.domain.usecase

import com.pluu.webtoon.NAV_ITEM
import com.pluu.webtoon.data.dao.IDBHelper
import com.pluu.webtoon.data.model.DBToon
import javax.inject.Inject

/**
 * Add Favorite Use Case
 */
class AddFavoriteUseCase @Inject constructor(
    private val dbHelper: IDBHelper
) {
    suspend operator fun invoke(type: NAV_ITEM, id: String) {
        dbHelper.addFavorite(
            DBToon(
                service = type.name,
                toonId = id
            )
        )
    }
}
