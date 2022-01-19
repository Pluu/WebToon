package com.pluu.webtoon.domain.usecase.site

import com.pluu.webtoon.data.repository.WebToonRepository
import com.pluu.webtoon.model.Result
import com.pluu.webtoon.model.ToonInfo
import com.pluu.webtoon.model.WeekPosition
import com.pluu.webtoon.model.map
import javax.inject.Inject

class GetWeeklyUseCase @Inject constructor(
    private val repository: WebToonRepository
) {
    suspend operator fun invoke(weekPosition: WeekPosition): Result<List<ToonInfo>> {
        return repository.getWeekly(weekPosition)
            .map { list ->
                list.distinctBy { it.id }
            }
    }
}