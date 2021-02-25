package com.pluu.webtoon.ui.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Intro ViewModel */
class IntroViewModel @Inject constructor() : ViewModel() {
    private val _initLoading = MutableStateFlow(false)
    val observe: StateFlow<Boolean>
        get() = _initLoading

    init {
        viewModelScope.launch {
            delay(1_000L)
            _initLoading.value = true
        }
    }
}
