package com.pluu.webtoon.domain.usecase.site

import com.pluu.webtoon.data.repository.WebToonRepository
import javax.inject.Inject

class GetWebToonTabsUseCase @Inject constructor(
    private val repository: WebToonRepository
) {
    operator fun invoke(): Array<String> {
        return repository.getWebToonTabs()
    }
}