package com.pluu.webtoon.ui.intro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pluu.webtoon.utils.withMainDispatchers
import kotlinx.coroutines.experimental.*
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
        jobs += GlobalScope.launch {
            delay(1, TimeUnit.SECONDS)
            withMainDispatchers {
                _observe.value = Unit
            }
        }
    }

    override fun onCleared() {
        jobs.forEach { it.cancel() }
        super.onCleared()
    }
}
