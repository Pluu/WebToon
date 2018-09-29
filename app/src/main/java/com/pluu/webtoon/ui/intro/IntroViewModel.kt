package com.pluu.webtoon.ui.intro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pluu.webtoon.utils.coroutineLaunchWithMain
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.delay
import java.util.concurrent.TimeUnit

/**
 * Intro ViewModel
 */
class IntroViewModel : ViewModel() {
    private val jobs = arrayListOf<Job>()

    private val _observe = MutableLiveData<Unit>()
    val observe: LiveData<Unit>
        get() = _observe

    init {
        jobs += coroutineLaunchWithMain {
            delay(TimeUnit.SECONDS.toMillis(1L))
            _observe.value = Unit
        }
    }

    override fun onCleared() {
        jobs.forEach { it.cancel() }
        super.onCleared()
    }
}
