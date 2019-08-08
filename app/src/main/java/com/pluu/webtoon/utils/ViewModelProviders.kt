package com.pluu.webtoon.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

///////////////////////////////////////////////////////////////////////////
// Simple ViewModelProvider Creator
///////////////////////////////////////////////////////////////////////////

inline fun <reified VM : ViewModel> FragmentActivity.viewModelProvider(): VM {
    return ViewModelProvider(viewModelStore, defaultViewModelProviderFactory).get(VM::class.java)
}

inline fun <reified VM : ViewModel> Fragment.viewModelProvider(): VM {
    return ViewModelProvider(viewModelStore, defaultViewModelProviderFactory).get(VM::class.java)
}

inline fun <reified VM : ViewModel> FragmentActivity.viewModelProvider(
    factory: ViewModelProvider.Factory
): VM {
    return ViewModelProvider(viewModelStore, factory).get(VM::class.java)
}

inline fun <reified VM : ViewModel> Fragment.viewModelProvider(
    factory: ViewModelProvider.Factory
): VM {
    return ViewModelProvider(viewModelStore, factory).get(VM::class.java)
}
