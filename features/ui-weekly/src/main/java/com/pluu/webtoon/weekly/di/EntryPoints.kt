package com.pluu.webtoon.weekly.di

import com.pluu.webtoon.weekly.ui.WeeklyDayViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@EntryPoint
@InstallIn(ActivityComponent::class)
internal interface ViewModelFactoryProvider {
    fun weeklyViewModelFactory(): WeeklyDayViewModel.Factory
}