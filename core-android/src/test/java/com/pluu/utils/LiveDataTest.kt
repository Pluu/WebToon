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
    fun observeNonNull() {
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
    fun observeNonNull_null() {
        val source = MutableLiveData<String>()
        var actual: String? = null
        val actionLambda: (String) -> Unit = { value ->
            actual = value
        }
        source.observeNonNull(lifecycleOwner, actionLambda)

        source.value = null
        assertNull(actual)
    }
}