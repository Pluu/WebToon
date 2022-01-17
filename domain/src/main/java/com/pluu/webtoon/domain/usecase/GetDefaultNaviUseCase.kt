package com.pluu.webtoon.domain.usecase

import com.pluu.webtoon.data.repository.WebToonCacheRepository
import javax.inject.Inject

class GetDefaultNaviUseCase @Inject constructor(
    private val repository: WebToonCacheRepository
) {
    operator fun invoke() = repository.getDefaultWebToon()
}