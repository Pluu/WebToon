package com.pluu.webtoon.intro.sample.di

import android.content.Context
import com.pluu.utils.toast
import com.pluu.webtoon.navigator.WeeklyNavigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal object NavigatorModule {
    @Provides
    fun provideWeeklyNavigator() = object : WeeklyNavigator {
        override fun openWeekly(context: Context) {
            context.toast("Show Weekly activity")
        }
    }
}

