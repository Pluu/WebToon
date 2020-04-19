package com.pluu.webtoon.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import androidx.annotation.StringRes
import com.pluu.webtoon.databinding.ViewProgressDialogBinding

class ProgressDialog {
    companion object {
        fun create(
            context: Context,
            @StringRes resId: Int
        ): Dialog {
            val binding = ViewProgressDialogBinding.inflate(
                LayoutInflater.from(context),
                null,
                false
            )
            binding.message.setText(resId)

            return Dialog(context).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                setContentView(binding.root)
            }
        }

        fun show(
            context: Context,
            @StringRes resId: Int
        ): Dialog {
            val dialog = create(context, resId)
            dialog.show()
            return dialog
        }
    }
}