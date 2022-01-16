package com.pluu.webtoon.domain.usecase

import com.pluu.webtoon.data.repository.WebToonRepository
import com.pluu.webtoon.model.NAV_ITEM
import com.pluu.webtoon.model.Toon
import javax.inject.Inject

/**
 * Delete Favorite Use Case
 */
class RemoveFavoriteUseCase @Inject constructor(
    private val repository: WebToonRepository
) {
    suspend operator fun invoke(type: NAV_ITEM, id: String) {
        repository.removeFavorite(
            Toon(
                service = type.name,
                toonId = id
            )
        )
    }
}
