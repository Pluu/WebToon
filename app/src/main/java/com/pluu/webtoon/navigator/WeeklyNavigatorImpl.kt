package com.pluu.webtoon.navigator

import android.content.Context
import com.pluu.utils.startActivity
import com.pluu.webtoon.ui.NavigationActivity
import javax.inject.Inject

internal class WeeklyNavigatorImpl @Inject constructor() : WeeklyNavigator {
    override fun openWeekly(context: Context) {
        context.startActivity<NavigationActivity>()
    }
}