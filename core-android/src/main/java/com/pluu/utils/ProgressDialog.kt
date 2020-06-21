package com.pluu.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import android.widget.TextView
import androidx.annotation.StringRes
import com.pluu.webtoon.core.R

class ProgressDialog {
    companion object {
        fun create(
            context: Context,
            @StringRes resId: Int
        ): Dialog {
            val view = LayoutInflater.from(context).inflate(
                R.layout.view_progress_dialog, null, false
            )
            view.findViewById<TextView>(R.id.message).setText(resId)
            return Dialog(context).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                setContentView(view)
            }
        }

        fun show(
            context: Context,
            @StringRes resId: Int
        ): Dialog {
            val dialog =
                create(context, resId)
            dialog.show()
            return dialog
        }
    }
}