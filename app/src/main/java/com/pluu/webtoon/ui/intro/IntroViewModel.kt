package com.pluu.webtoon.ui.intro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pluu.webtoon.utils.AppCoroutineDispatchers
import com.pluu.webtoon.utils.launch
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

/**
 * Intro ViewModel
 */
class IntroViewModel(
    dispatchers: AppCoroutineDispatchers,
    private val useCase: IntroUseCase
) : ViewModel() {

    private val _observe = MutableLiveData<Unit>()
    val observe: LiveData<Unit>
        get() = _observe

    init {
        dispatchers.main.launch {
            initAction()
        }
    }

    private suspend fun initAction() {
        delay(TimeUnit.SECONDS.toMillis(1L))
        useCase.init()
        _observe.value = Unit
    }
}
