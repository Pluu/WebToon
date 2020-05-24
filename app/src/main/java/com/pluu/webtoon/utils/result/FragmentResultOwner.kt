package com.pluu.webtoon.utils.result

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.setFragmentResultListener

fun FragmentActivity.setFragmentResultListener(
    requestKey: String,
    listener: (resultKey: String, bundle: Bundle) -> Unit
) {
    supportFragmentManager.setFragmentResultListener(requestKey, this, listener)
}