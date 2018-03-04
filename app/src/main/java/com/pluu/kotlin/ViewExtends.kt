package com.pluu.kotlin

import android.view.View

fun Boolean.toVisibleOrGone() = if (this) View.VISIBLE else View.GONE