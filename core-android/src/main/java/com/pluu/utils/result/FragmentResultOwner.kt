package com.pluu.utils.result

import android.os.Bundle
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.setFragmentResultListener(
    requestKey: String,
    listener: (resultKey: String, bundle: Bundle) -> Unit
) {
    supportFragmentManager.setFragmentResultListener(requestKey, this,
        { _requestKey, bundle -> listener.invoke(_requestKey, bundle) }
    )
}