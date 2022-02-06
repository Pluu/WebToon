package com.pluu.webtoon.navigation.customtabs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.get
import androidx.navigation.navArgument
import com.pluu.webtoon.navigation.customtabs.ChromeCustomTabsNavigator.Companion.KEY_ROUTE
import com.pluu.webtoon.navigation.customtabs.ChromeCustomTabsNavigator.Companion.KEY_URL

fun NavGraphBuilder.chromeCustomTabs() {
    addDestination(
        ChromeCustomTabsNavigator.Destination(
            provider[ChromeCustomTabsNavigator::class]
        ).apply {
            val route = "chrome/{$KEY_URL}"
            val internalRoute = createRoute(route)
            addDeepLink(internalRoute)
            addArgument(
                KEY_ROUTE, navArgument(KEY_ROUTE) { defaultValue = route }.argument
            )
            id = internalRoute.hashCode()
            addArgument(
                KEY_URL, navArgument(KEY_URL) { type = NavType.StringType }.argument
            )
        }
    )
}