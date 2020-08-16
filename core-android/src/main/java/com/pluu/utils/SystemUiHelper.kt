package com.pluu.utils

import android.view.View
import android.view.Window

object SystemUiHelper {
    fun toggleHideBar(window: Window): Boolean {
        return toggleHideBar21(window)
    }

    private fun toggleHideBar21(window: Window): Boolean {
        val uiOptions = window.decorView.systemUiVisibility
        val newUiOptions = uiOptions xor View.SYSTEM_UI_FLAG_LOW_PROFILE
        window.decorView.systemUiVisibility = newUiOptions
        return uiOptions and View.SYSTEM_UI_FLAG_LOW_PROFILE == 0
    }
}