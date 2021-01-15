package com.pluu.webtoon.ui.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/** Intro ViewModel */
class IntroViewModel @Inject constructor() : ViewModel() {
    // TOOD: 로딩 움직임이 멈추는 현상
    val observe = flow {
        delay(1000L)
        emit(Unit)
    }.asLiveData()
}
