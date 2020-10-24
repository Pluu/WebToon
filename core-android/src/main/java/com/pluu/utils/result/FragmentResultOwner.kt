package com.pluu.utils.result

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.setFragmentResultListener(
    requestKey: String,
    listener: (resultKey: String, bundle: Bundle) -> Unit
) {
    supportFragmentManager.setFragmentResultListener(requestKey, this,
        { _requestKey, bundle -> listener.invoke(_requestKey, bundle) }
    )
}

fun Fragment.setFragmentResult(
    requestKey: String,
    result: Bundle = Bundle()
) = parentFragmentManager.setFragmentResult(requestKey, result)
