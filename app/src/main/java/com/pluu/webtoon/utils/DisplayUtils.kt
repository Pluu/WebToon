package com.pluu.webtoon.utils

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.pluu.webtoon.R

/**
 * Display Utils
 * Created by pluu on 2017-04-18.
 */
fun AppCompatActivity.animatorToolbarColor(color: Int) =
    animatorToolbarColor(color, ValueAnimator.AnimatorUpdateListener {
        supportActionBar?.apply {
            setBackgroundDrawable(ColorDrawable(it.animatedValue as Int))
        }
    })

fun AppCompatActivity.animatorToolbarColor(
    color: Int,
    listener: ValueAnimator.AnimatorUpdateListener?
): ValueAnimator {
    val value = resolveAttribute(R.attr.colorPrimary)

    val animator = ValueAnimator.ofObject(ArgbEvaluator(), value.data, color)
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

fun Activity.animatorStatusBarColor(color: Int): ValueAnimator {
    val value = resolveAttribute(R.attr.colorPrimaryDark)

    return ValueAnimator.ofObject(ArgbEvaluator(), value.data, color).apply {
        addUpdateListener { animation ->
            this@animatorStatusBarColor.setStatusBarColor(animation.animatedValue as Int)
        }
        interpolator = DecelerateInterpolator()
    }
}
