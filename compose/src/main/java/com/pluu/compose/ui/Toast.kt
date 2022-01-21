package com.pluu.compose.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast as AndroidToast

@Composable
fun toast(text: CharSequence) {
    toast(text, AndroidToast.LENGTH_SHORT)
}

@Composable
fun longToast(text: CharSequence) {
    toast(text, AndroidToast.LENGTH_LONG)
}

@Composable
private fun toast(text: CharSequence, duration: Int) {
    AndroidToast.makeText(LocalContext.current, text, duration).show()
}