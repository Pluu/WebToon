package com.pluu.webtoon.domain.usecase

import com.pluu.webtoon.data.repository.WebToonRepository
import com.pluu.webtoon.model.NAV_ITEM
import javax.inject.Inject

/**
 * 즐겨찾기 여부 UseCase
 */
class HasFavoriteUseCase @Inject constructor(
    private val repository: WebToonRepository
) {
    /**
     * 즐겨찾기 여부 판단
     *
     * @param type Type
     * @param id 웹툰 ID
     * @return true/false
     */
    suspend operator fun invoke(type: NAV_ITEM, id: String): Boolean =
        repository.isFavorite(type.name, id)
}
