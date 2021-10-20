package com.pluu.webtoon.weekly.sample.di

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.pluu.utils.toast
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.navigator.EpisodeNavigator
import com.pluu.webtoon.navigator.SettingNavigator
import com.pluu.webtoon.ui.model.PalletColor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal object NavigatorModule {
    @Provides
    fun provideSettingNavigator() = object : SettingNavigator {
        override fun openSetting(context: Context) {
            context.toast("Show Setting activity")
        }

        override fun openLicense(context: Context) {
            context.toast("Show License activity")
        }
    }

    @Provides
    fun provideEpisodeNavigator() = object : EpisodeNavigator {
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

