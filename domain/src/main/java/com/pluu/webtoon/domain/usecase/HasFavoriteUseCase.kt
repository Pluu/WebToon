package com.pluu.webtoon.domain.usecase

import com.pluu.webtoon.NAV_ITEM
import com.pluu.webtoon.data.db.IDBHelper

/**
 * 즐겨찾기 여부 UseCase
 */
class HasFavoriteUseCase(
    private val dbHelper: IDBHelper,
    private val naviItem: NAV_ITEM
) {
    /**
     * 즐겨찾기 여부 판단
     * @param id 웹툰 ID
     * @return true/false
     */
    suspend operator fun invoke(id: String): Boolean =
        dbHelper.isFavorite(naviItem.name, id)
}
