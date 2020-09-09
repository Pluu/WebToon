package com.pluu.utils

import android.os.Build
import android.view.View
import android.view.Window
import androidx.annotation.RequiresApi

object SystemUiHelper {
    fun toggleHideBar(window: Window): Boolean {
        return if (Build.VERSION.SDK_INT >= 30) {
            Impl30.toggleSystemBarWindows(window)
        } else {
            Impl21.toggleSystemBarWindows(window)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private object Impl21 {
        fun toggleSystemBarWindows(
            window: Window
        ): Boolean {
            val uiOptions = window.decorView.systemUiVisibility
            val newUiOptions = uiOptions xor View.SYSTEM_UI_FLAG_LOW_PROFILE
            window.decorView.systemUiVisibility = newUiOptions
            return uiOptions and View.SYSTEM_UI_FLAG_LOW_PROFILE == 0
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private object Impl30 {
        private var isHide = false
        fun toggleSystemBarWindows(
            window: Window
        ): Boolean {
            isHide = !isHide
            return isHide
        }
    }
}