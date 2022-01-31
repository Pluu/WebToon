package com.pluu.webtoon.sample

import android.content.Context
import com.pluu.utils.toast
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.navigator.BrowserNavigator
import com.pluu.webtoon.navigator.DetailNavigator
import com.pluu.webtoon.navigator.EpisodeNavigator
import com.pluu.webtoon.navigator.SettingNavigator
import com.pluu.webtoon.navigator.WeeklyNavigator
import com.pluu.webtoon.ui.model.PalletColor

internal class BaseSampleNavigator : WeeklyNavigator, EpisodeNavigator, DetailNavigator,
    BrowserNavigator, SettingNavigator {

    override fun openWeekly(context: Context) {
        context.toast("Show Weekly activity")
    }

    override fun openEpisode(
        context: Context,
        item: ToonInfoWithFavorite,
        palletColor: PalletColor
    ) {
        context.toast("Show Episode activity")
    }

    override fun openDetail(
        context: Context,
        item: EpisodeInfo,
        palletColor: PalletColor
    ) {
        context.toast("Show Detail activity")
    }

    override fun openSetting(context: Context) {
        context.toast("Show Setting activity")
    }

    override fun openBrowser(context: Context, toolbarColor: Int, url: String) {
        context.toast("Show Browser activity")
    }
}