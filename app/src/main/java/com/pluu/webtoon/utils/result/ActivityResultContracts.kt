package com.pluu.webtoon.utils.result

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContracts

inline fun ActivityResultCaller.justSafeRegisterForActivityResult(
    input: Intent,
    crossinline callback: (ActivityResult) -> Unit
) {
    prepareCall(ActivityResultContracts.StartActivityForResult()) { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            callback(activityResult)
        }
    }.launch(input)
}
