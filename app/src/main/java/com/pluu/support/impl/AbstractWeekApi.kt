package com.pluu.support.impl

import android.content.Context
import androidx.core.content.ContextCompat
import com.pluu.webtoon.di.NetworkUseCase
import com.pluu.webtoon.item.WebToonInfo
import java.util.*

/**
 * Week API
 * Created by pluu on 2017-04-20.
 */
abstract class AbstractWeekApi protected constructor(
    networkUseCase: NetworkUseCase,
    private val CURRENT_TABS: Array<String>
) : NetworkSupportApi(networkUseCase) {

    abstract val naviItem: NAV_ITEM

    fun getTitleColor(context: Context): Int =
        ContextCompat.getColor(context, naviItem.color)

    fun getTitleColorDark(context: Context): Int =
        ContextCompat.getColor(context, naviItem.bgColor)

    val weeklyTabSize: Int
        get() = CURRENT_TABS.size

    fun getWeeklyTabName(position: Int): String =
        CURRENT_TABS[position]

    val todayTabPosition: Int
        get() = (Calendar.getInstance(Locale.getDefault()).get(Calendar.DAY_OF_WEEK) + 5) % 7

    @Throws(Exception::class)
    abstract fun parseMain(position: Int): List<WebToonInfo>
}
