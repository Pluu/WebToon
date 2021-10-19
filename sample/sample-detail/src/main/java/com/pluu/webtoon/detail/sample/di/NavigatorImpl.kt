package com.pluu.webtoon.sample.di

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.pluu.utils.toast
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.navigator.BrowserNavigator
import com.pluu.webtoon.navigator.DetailNavigator
import com.pluu.webtoon.navigator.EpisodeNavigator
import com.pluu.webtoon.navigator.SettingNavigator
import com.pluu.webtoon.ui.model.PalletColor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object NavigatorModule {
    @Provides
    fun provideDetailNavigator(): DetailNavigator = object : DetailNavigator {
        override fun openDetail(
            context: Context,
            launcher: ActivityResultLauncher<Intent>,
            item: EpisodeInfo,
            palletColor: PalletColor
        ) {
            context.toast("Show Detail activity")
        }
    }

    @Provides
    fun provideBrowserNavigator(): BrowserNavigator = object : BrowserNavigator {
        override fun openBrowser(context: Context, toolbarColor: Int, url: String) {
            context.toast("Show Browser activity")
        }
    }

    @Provides
    fun provideSettingNavigator(): SettingNavigator = object : SettingNavigator {
        override fun openSetting(context: Context) {
            context.toast("Show Setting activity")
        }

        override fun openLicense(context: Context) {
            context.toast("Show License activity")
        }
    }

    @Provides
    fun provideEpisodeNavigator(): EpisodeNavigator = object : EpisodeNavigator {
        override fun openEpisode(
            context: Context,
            launcher: ActivityResultLauncher<Intent>,
            item: ToonInfoWithFavorite,
            palletColor: PalletColor
        ) {
            context.toast("Show Episode activity")
        }
    }
}

