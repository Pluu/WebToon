package com.pluu.webtoon.weekly.ui

import androidx.lifecycle.ViewModel
import com.pluu.webtoon.domain.usecase.site.GetWebToonTabsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class WeeklyViewModel @Inject constructor(
    private val getWebToonTabsUseCase: GetWebToonTabsUseCase
) : ViewModel() {
    fun getTabs(): List<String> = getWebToonTabsUseCase()
}
