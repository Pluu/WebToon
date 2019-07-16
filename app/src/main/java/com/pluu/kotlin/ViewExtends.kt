package com.pluu.kotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

@Suppress("UNCHECKED_CAST")
fun <T : View> ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = true): T =
    LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot) as T
