package com.pluu.webtoon.navigation.customtabs

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
import androidx.navigation.navOptions
import java.net.URLEncoder

@Composable
fun rememberNavController(
    vararg navigators: Navigator<out NavDestination>
): NavHostController {
    val context = LocalContext.current
    return rememberSaveable(inputs = navigators, saver = navControllerSaver(context)) {
        createNavController(context)
    }.apply {
        for (navigator in navigators) {
            navigatorProvider.addNavigator(navigator)
        }
    }
}

private fun createNavController(context: Context) =
    NavHostController(context).apply {
        navigatorProvider.addNavigator(ComposeNavigator())
        navigatorProvider.addNavigator(DialogNavigator())
        navigatorProvider.addNavigator(ChromeCustomTabsNavigator(context))
    }

private fun navControllerSaver(
    context: Context
): Saver<NavHostController, *> = Saver(
    save = { it.saveState() },
    restore = { createNavController(context).apply { restoreState(it) } }
)

fun NavController.navigateChromeCustomTabs(
    url: String,
    builder: NavOptionsBuilder.() -> Unit = {},
    extraBuilder: ChromeCustomTabsNavigator.Extras.Builder.() -> Unit = { },
) {
    navigate(
        NavDeepLinkRequest.Builder.fromUri(
            createRoute("chrome/${URLEncoder.encode(url, "utf-8")}").toUri()
        ).build(),
        navOptions(builder),
        ChromeCustomTabsNavigator.Extras.Builder().apply {
            extraBuilder()
        }.build()
    )
}

internal fun createRoute(route: String) = "android-app://androidx.navigation.chrome/$route"