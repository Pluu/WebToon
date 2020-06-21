package com.pluu.webtoon.domain.usecase

import com.pluu.webtoon.domain.moel.DetailResult
import com.pluu.webtoon.domain.moel.EpisodeResult
import com.pluu.webtoon.domain.moel.ToonInfo
import com.pluu.webtoon.domain.usecase.param.DetailRequest
import com.pluu.webtoon.domain.usecase.param.EpisodeRequest
import com.pluu.webtoon.domain.usecase.param.WeeklyRequest
import com.pluu.webtoon.model.Result
import java.util.Calendar
import java.util.Locale

interface WeeklyUseCase {
    val CURRENT_TABS: Array<String>

    val weeklyTabSize: Int
        get() = CURRENT_TABS.size

    fun getWeeklyTabName(position: Int): String =
        CURRENT_TABS[position]

    val todayTabPosition: Int
        get() = (Calendar.getInstance(Locale.getDefault()).get(Calendar.DAY_OF_WEEK) + 5) % 7

    @Throws(Exception::class)
    suspend operator fun invoke(param: WeeklyRequest): Result<List<ToonInfo>>
}

interface EpisodeUseCase {
    suspend operator fun invoke(param: EpisodeRequest): Result<EpisodeResult>
}

interface DetailUseCase {
    suspend operator fun invoke(param: DetailRequest): DetailResult
}
