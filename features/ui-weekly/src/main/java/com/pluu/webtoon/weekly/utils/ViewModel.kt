package com.pluu.webtoon.weekly.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.DEFAULT_ARGS_KEY
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import dagger.hilt.android.internal.lifecycle.HiltViewModelFactory

///////////////////////////////////////////////////////////////////////////
// Origin : hilt/hilt-navigation-compose/src/main/java/androidx/hilt/navigation/compose/HiltViewModel.kt
///////////////////////////////////////////////////////////////////////////

@Composable
inline fun <reified VM : ViewModel> hiltViewModelWithAdditional(
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    key: String? = null,
    extras: CreationExtras = if (viewModelStoreOwner is HasDefaultViewModelProviderFactory) {
        viewModelStoreOwner.defaultViewModelCreationExtras
    } else {
        CreationExtras.Empty
    },
    additionalExtras: Bundle? = null
): VM {
    val factory = createHiltViewModelFactory(viewModelStoreOwner, additionalExtras)
    return viewModel(
        viewModelStoreOwner,
        key,
        factory = factory,
        extras = MutableCreationExtras(extras).apply {
            if (additionalExtras != null) {
                set(DEFAULT_ARGS_KEY, additionalExtras)
            }
        })
}

@Composable
@PublishedApi
internal fun createHiltViewModelFactory(
    viewModelStoreOwner: ViewModelStoreOwner,
    additionalExtras: Bundle? = null
): ViewModelProvider.Factory? = if (viewModelStoreOwner is NavBackStackEntry) {
    HiltViewModelAdditionalFactory(
        context = LocalContext.current,
        navBackStackEntry = viewModelStoreOwner,
        additionalExtras = additionalExtras
    )
} else {
    // Use the default factory provided by the ViewModelStoreOwner
    // and assume it is an @AndroidEntryPoint annotated fragment or activity
    null
}

///////////////////////////////////////////////////////////////////////////
// Origin : hilt/hilt-navigation/src/main/java/androidx/hilt/navigation/HiltNavBackStackEntry.kt
///////////////////////////////////////////////////////////////////////////

private fun HiltViewModelAdditionalFactory(
    context: Context,
    navBackStackEntry: NavBackStackEntry,
    additionalExtras: Bundle? = null
): ViewModelProvider.Factory {
    val activity = context.let {
        var ctx = it
        while (ctx is ContextWrapper) {
            if (ctx is Activity) {
                return@let ctx
            }
            ctx = ctx.baseContext
        }
        throw IllegalStateException(
            "Expected an activity context for creating a HiltViewModelFactory for a " +
                    "NavBackStackEntry but instead found: $ctx"
        )
    }
    val extras = Bundle()
    if (navBackStackEntry.arguments != null) {
        extras.putAll(navBackStackEntry.arguments)
    }
    if (additionalExtras != null) {
        extras.putAll(additionalExtras)
    }
    return HiltViewModelFactory.createInternal(
        activity,
        navBackStackEntry,
        extras,
        navBackStackEntry.defaultViewModelProviderFactory,
    )
}