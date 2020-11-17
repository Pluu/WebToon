package com.pluu.webtoon.ui.intro

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

/** Intro ViewModel */
class IntroViewModel @ViewModelInject constructor() : ViewModel() {
    val observe = flow {
        delay(1000L)
        emit(Unit)
    }.asLiveData()
}
