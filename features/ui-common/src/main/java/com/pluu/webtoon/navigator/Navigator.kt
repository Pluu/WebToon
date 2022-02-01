package com.pluu.webtoon.navigator

import android.content.Context
import androidx.annotation.ColorInt

interface WeeklyNavigator {
    /** Weekly 화면으로 이동 */
    fun openWeekly(context: Context)
}

interface BrowserNavigator {
    fun openBrowser(
        context: Context,
        @ColorInt toolbarColor: Int,
        url: String
    )
}