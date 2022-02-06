package com.pluu.webtoon.sample

import android.content.Context
import com.pluu.utils.toast
import com.pluu.webtoon.navigator.WeeklyNavigator

internal class BaseSampleNavigator : WeeklyNavigator {

    override fun openWeekly(context: Context) {
        context.toast("Show Weekly activity")
    }

}