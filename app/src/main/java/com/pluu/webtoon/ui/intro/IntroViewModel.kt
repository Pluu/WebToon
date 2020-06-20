package com.pluu.webtoon.ui.intro

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

/**
 * Intro ViewModel
 */
class IntroViewModel @ViewModelInject constructor() : ViewModel() {

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
        _observe.value = Unit
    }
}
