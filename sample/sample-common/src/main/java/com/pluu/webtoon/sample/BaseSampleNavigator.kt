package com.pluu.webtoon.sample

import android.content.Context
import com.pluu.utils.toast
import com.pluu.webtoon.navigator.BrowserNavigator
import com.pluu.webtoon.navigator.WeeklyNavigator

internal class BaseSampleNavigator : WeeklyNavigator, BrowserNavigator {

    override fun openWeekly(context: Context) {
        context.toast("Show Weekly activity")
    }

    override fun openBrowser(context: Context, toolbarColor: Int, url: String) {
        context.toast("Show Browser activity")
    }
}