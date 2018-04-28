package com.pluu.kotlin

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun Boolean.toVisibleOrGone() = if (this) View.VISIBLE else View.GONE

@Suppress("UNCHECKED_CAST")
fun <T : View> ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = true): T =
    LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot) as T