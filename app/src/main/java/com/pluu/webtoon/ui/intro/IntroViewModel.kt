package com.pluu.webtoon.ui.intro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Intro ViewModel
 */
class IntroViewModel(
    private val useCase: IntroUseCase
) : ViewModel() {

    private val _observe = MutableLiveData<Unit>()
    val observe: LiveData<Unit>
        get() = _observe

    init {
        viewModelScope.launch {
            initAction()
        }
    }

    private suspend fun initAction() {
        delay(TimeUnit.SECONDS.toMillis(1L))
        useCase.init()
        _observe.value = Unit
    }
}
