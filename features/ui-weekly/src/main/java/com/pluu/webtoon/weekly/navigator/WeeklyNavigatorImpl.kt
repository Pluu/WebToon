package com.pluu.webtoon.weekly.navigator

import android.content.Context
import com.pluu.utils.startActivity
import com.pluu.webtoon.navigator.WeeklyNavigator
import com.pluu.webtoon.weekly.ui.WeeklyActivity
import javax.inject.Inject

internal class WeeklyNavigatorImpl @Inject constructor() : WeeklyNavigator {
    override fun openWeekly(context: Context) {
        context.startActivity<WeeklyActivity>()
    }
}