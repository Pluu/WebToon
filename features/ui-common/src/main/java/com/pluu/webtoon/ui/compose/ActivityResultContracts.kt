package com.pluu.webtoon.ui.compose

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
inline fun rememberLauncherForActivityResult(
    crossinline callback: (ActivityResult) -> Unit
) =
    rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            callback(activityResult)
        }
    }