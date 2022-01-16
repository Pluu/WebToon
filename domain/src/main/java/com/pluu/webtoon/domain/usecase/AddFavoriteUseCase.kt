package com.pluu.webtoon.domain.usecase

import com.pluu.webtoon.data.repository.WebToonRepository
import com.pluu.webtoon.model.NAV_ITEM
import com.pluu.webtoon.model.Toon
import javax.inject.Inject

/**
 * Add Favorite Use Case
 */
class AddFavoriteUseCase @Inject constructor(
    private val repository: WebToonRepository
) {
    suspend operator fun invoke(type: NAV_ITEM, id: String) {
        repository.addFavorite(
            Toon(
                service = type.name,
                toonId = id
            )
        )
    }
}
