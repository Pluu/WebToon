package com.pluu.webtoon.ui.intro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pluu.webtoon.utils.launchWithUI
import com.pluu.webtoon.utils.lazyNone
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

/**
 * Intro ViewModel
 */
class IntroViewModel(
    private val useCase: IntroUseCase
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    private val job by lazyNone { Job() }

    private val _observe = MutableLiveData<Unit>()
    val observe: LiveData<Unit>
        get() = _observe

    init {
        launchWithUI {
            initAction()
        }
    }

    private suspend fun initAction() {
        delay(TimeUnit.SECONDS.toMillis(1L))
        useCase.init()
        _observe.value = Unit
    }

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }
}
