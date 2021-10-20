package com.pluu.webtoon.episode.sample.di

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.pluu.utils.toast
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.navigator.BrowserNavigator
import com.pluu.webtoon.navigator.DetailNavigator
import com.pluu.webtoon.ui.model.PalletColor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal object NavigatorModule {
    @Provides
    fun provideDetailNavigator() = object : DetailNavigator {
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
    fun provideBrowserNavigator() = object : BrowserNavigator {
        override fun openBrowser(context: Context, toolbarColor: Int, url: String) {
            context.toast("Show Browser activity")
        }
    }
}

