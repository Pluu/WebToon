package com.pluu.webtoon.utils

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import timber.log.Timber

///////////////////////////////////////////////////////////////////////////
// 소스 참고 : https://gist.github.com/Aidanvii7/46f9f014ba4dc58a2ac10625b198769d
///////////////////////////////////////////////////////////////////////////

fun NavController.navigateWithArgument(
    route: String,
    args: List<Pair<String, Any>>? = null,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    if (currentDestination?.route == route) {
        Timber.tag("Logger").d("Skip Navigate ${currentDestination?.route}")
        return
    }
    navigate(route, navOptions, navigatorExtras)

    if (args.isNullOrEmpty()) {
        return
    }
    val bundle = backQueue.lastOrNull()?.arguments
    if (bundle != null) {
        bundle.putAll(bundleOf(*args.toTypedArray()))
    } else {
        Timber.w("The last argument of NavBackStackEntry is null.")
    }
}