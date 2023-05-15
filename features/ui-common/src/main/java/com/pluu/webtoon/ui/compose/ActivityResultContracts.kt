package com.pluu.webtoon.ui.compose

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
inline fun rememberLauncherForActivityResult(
    crossinline callback: (ActivityResult) -> Unit
): ActivityResultLauncher<Intent> =
    rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            callback(activityResult)
        }
    }