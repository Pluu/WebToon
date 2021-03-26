package com.pluu.webtoon.ui.compose

import android.app.Activity
import androidx.activity.compose.registerForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
inline fun registerStartActivityForResult(
    crossinline callback: (ActivityResult) -> Unit
) = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
    if (activityResult.resultCode == Activity.RESULT_OK) {
        callback(activityResult)
    }
}