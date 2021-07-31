package com.pluu.utils.result

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

inline fun ComponentActivity.registerForActivityResult(
    crossinline callback: (ActivityResult) -> Unit
) = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
    if (activityResult.resultCode == Activity.RESULT_OK) {
        callback(activityResult)
    }
}

inline fun Fragment.registerForActivityResult(
    crossinline callback: (ActivityResult) -> Unit
) = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
    if (activityResult.resultCode == Activity.RESULT_OK) {
        callback(activityResult)
    }
}
