package com.pluu.webtoon.domain.base

import com.pluu.webtoon.data.model.Result
import com.pluu.webtoon.domain.moel.ToonInfo
import com.pluu.webtoon.domain.usecase.param.WeeklyRequest
import java.util.Calendar
import java.util.Locale

/**
 * Week API
 * Created by pluu on 2017-04-20.
 */
interface AbstractWeekApi {

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
