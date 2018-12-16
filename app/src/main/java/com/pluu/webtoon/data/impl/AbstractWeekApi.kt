package com.pluu.webtoon.data.impl

import com.pluu.webtoon.data.WeeklyRequest
import com.pluu.webtoon.item.Result
import com.pluu.webtoon.item.ToonInfo
import java.util.*

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
