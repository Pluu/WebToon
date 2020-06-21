package com.pluu.utils

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.os.Build
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import androidx.annotation.AttrRes

fun animatorColor(
    startColor: Int,
    endColor: Int,
    listener: ValueAnimator.AnimatorUpdateListener? = null
): ValueAnimator {
    val animator = ValueAnimator.ofObject(ArgbEvaluator(), startColor, endColor)
    if (listener != null) {
        animator.addUpdateListener(listener)
    }
    animator.interpolator = DecelerateInterpolator()
    return animator
}

fun Activity.setStatusBarColor(color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            statusBarColor = color
        }
    }
}

inline fun Activity.animatorColorFromThemeAttrId(
    @AttrRes themeAttrId: Int,
    color: Int,
    interpolator: Interpolator = DecelerateInterpolator(),
    crossinline updateAction: (Int) -> Unit = {}
): ValueAnimator {
    return animatorColor(
        startColor = getThemeColor(themeAttrId),
        endColor = color
    ).apply {
        addUpdateListener { animation ->
            updateAction(animation.animatedValue as Int)
        }
        this.interpolator = interpolator
    }
}

