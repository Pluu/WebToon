package com.pluu.webtoon.navigation.customtabs

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import java.net.URLEncoder

@Composable
fun rememberWebToonNavController(): NavHostController {
    val context = LocalContext.current
    val chromeNavigator = ChromeCustomTabsNavigator(context)
    return rememberNavController(chromeNavigator)
}

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