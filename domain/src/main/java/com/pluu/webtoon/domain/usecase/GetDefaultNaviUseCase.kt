package com.pluu.webtoon.domain.usecase

import com.pluu.webtoon.data.repository.SessionRepository
import javax.inject.Inject

class GetDefaultNaviUseCase @Inject constructor(
    private val repository: SessionRepository
) {
    operator fun invoke() = repository.getDefaultWebToon()
}