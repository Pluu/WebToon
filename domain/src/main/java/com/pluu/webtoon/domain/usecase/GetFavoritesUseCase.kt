package com.pluu.webtoon.domain.usecase

import com.pluu.webtoon.data.repository.WebToonRepository
import com.pluu.webtoon.model.NAV_ITEM
import com.pluu.webtoon.model.ToonId
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 즐겨찾기 여부 UseCase
 */
class GetFavoritesUseCase @Inject constructor(
    private val repository: WebToonRepository
) {
    /**
     * 즐겨찾기 여부 판단
     *
     * @param type Type
     * @return true/false
     */
    operator fun invoke(type: NAV_ITEM): Flow<Set<ToonId>> =
        repository.getFavorites(type.name)
}
