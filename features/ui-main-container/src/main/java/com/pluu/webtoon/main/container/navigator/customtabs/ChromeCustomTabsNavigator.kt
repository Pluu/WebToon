package com.pluu.webtoon.main.container.navigator.customtabs

///////////////////////////////////////////////////////////////////////////
// 참고 소스 : https://github.com/androidx/androidx/blob/androidx-main/navigation/navigation-fragment/src/main/java/androidx/navigation/fragment/FragmentNavigator.kt
///////////////////////////////////////////////////////////////////////////

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.annotation.ColorInt
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

@Navigator.Name("chrome")
class ChromeCustomTabsNavigator(
    private val context: Context
) : Navigator<ChromeCustomTabsNavigator.Destination>() {

    override fun createDestination(): Destination = Destination(this)

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        val url = checkNotNull(args!!.getString(KEY_URL)) {
            "Destination ${destination.id} does not have an url."
        }
        val customTabsIntent = CustomTabsIntent.Builder().apply {
            if (navigatorExtras is Extras) {
                buildCustomTabsIntent(navigatorExtras)
            }
        }.build()
        customTabsIntent.launchUrl(context, url.toUri())
        return null // Do not add to the back stack, managed by Chrome Custom Tabs
    }

    override fun popBackStack(): Boolean = true // Managed by Chrome Custom Tabs

    private fun CustomTabsIntent.Builder.buildCustomTabsIntent(
        extras: Extras
    ): CustomTabsIntent {
        val colorBuilder = CustomTabColorSchemeParams.Builder()
        if (extras.toolbarColor != null) {
            colorBuilder.setToolbarColor(extras.toolbarColor)
        }
        setDefaultColorSchemeParams(colorBuilder.build())

        val customTabsIntent = build()

        // Adding referrer so websites know where their traffic came from, per Google's recommendations:
        // https://medium.com/google-developers/best-practices-for-custom-tabs-5700e55143ee
        customTabsIntent.intent.putExtra(
            Intent.EXTRA_REFERRER,
            Uri.parse("android-app://" + context.packageName)
        )

        return customTabsIntent
    }

    @NavDestination.ClassType(Activity::class)
    class Destination(navigator: ChromeCustomTabsNavigator) : NavDestination(navigator)

    class Extras internal constructor(
        internal val toolbarColor: Int?
    ) : Navigator.Extras {
        class Builder {
            @ColorInt
            private var _toolbarColor: Int? = null

            fun setToolbarColor(@ColorInt color: Int): Builder = apply {
                _toolbarColor = color
            }

            fun build(): Extras {
                return Extras(_toolbarColor)
            }
        }
    }

    companion object {
        internal const val KEY_URL = "url"
        internal const val KEY_ROUTE = "android-support-nav:controller:chrome"
    }
}