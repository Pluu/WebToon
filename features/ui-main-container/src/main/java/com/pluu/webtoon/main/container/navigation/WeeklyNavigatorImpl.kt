package com.pluu.webtoon.main.container.navigation

import android.content.Context
import com.pluu.utils.startActivity
import com.pluu.webtoon.main.container.ui.NavigationActivity
import com.pluu.webtoon.navigator.WeeklyNavigator
import javax.inject.Inject

internal class WeeklyNavigatorImpl @Inject constructor() : WeeklyNavigator {
    override fun openWeekly(context: Context) {
        context.startActivity<NavigationActivity>()
    }
}