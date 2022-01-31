package com.pluu.webtoon.utils

import android.os.Bundle
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import timber.log.Timber

fun NavHostController.safeNavigate(
    route: String,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    if (currentDestination?.route == route) {
        Timber.tag("Logger").d("Skip Navigate ${currentDestination?.route}")
        return
    }
    navigate(route, navOptions, navigatorExtras)
}

fun NavHostController.provideLocalSavedHandle(
    action: Bundle.() -> Unit
) {
    val bundle = backQueue.lastOrNull()?.arguments
    if (bundle != null) {
        bundle.action()
    } else {
        Timber.e("The last argument of NavBackStackEntry is null.")
    }
}