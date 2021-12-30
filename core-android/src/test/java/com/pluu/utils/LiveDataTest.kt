package com.pluu.utils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.testing.TestLifecycleOwner
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class LiveDataTest {

    @get:Rule
    val mInstantTaskExecutorRule = InstantTaskExecutorRule()

    private val lifecycleOwner = TestLifecycleOwner()

    @Test
    fun livedata_nonNull_input() {
        val source = MutableLiveData<String>()
        var actual: String? = null
        val actionLambda: (String) -> Unit = { value ->
            actual = value
        }
        source.observeNonNull(lifecycleOwner, actionLambda)

        source.value = "Test"
        assertEquals("Test", actual)
    }

    @Test
    fun livedata_null_input() {
        val source = MutableLiveData<String>()
        var actual: String? = null
        val actionLambda: (String) -> Unit = { value ->
            actual = value
        }
        source.observeNonNull(lifecycleOwner, actionLambda)

        source.value = null
        assertNull(actual)
    }

    @Test
    fun nullable_livedata_nonNull_input() {
        val source = MutableLiveData<String?>()
        var actual: String? = null
        val actionLambda: (String) -> Unit = { value ->
            actual = value
        }
        source.observeNonNull(lifecycleOwner, actionLambda)

        source.value = "Test"
        assertEquals("Test", actual)
    }

    @Test
    fun nullable_livedata_null_input() {
        val source = MutableLiveData<String?>()
        var actual: String? = null
        val actionLambda: (String) -> Unit = { value ->
            actual = value
        }
        source.observeNonNull(lifecycleOwner, actionLambda)

        source.value = null
        assertNull(actual)
    }
}