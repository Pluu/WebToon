package com.pluu.webtoon.intro.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Intro ViewModel */
@HiltViewModel
internal class IntroViewModel @Inject constructor() : ViewModel() {
    private val _initLoading = MutableStateFlow(false)
    val observe: StateFlow<Boolean> get() = _initLoading

    init {
        viewModelScope.launch {
            delay(1_000L)
            _initLoading.value = true
        }
    }
}
