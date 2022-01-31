package com.pluu.webtoon.ui

import android.os.Bundle
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pluu.compose.runtime.rememberMutableStateOf
import com.pluu.webtoon.model.CurrentSession
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.setting.ui.LicenseUi
import com.pluu.webtoon.setting.ui.SettingsUi
import com.pluu.webtoon.ui.model.PalletColor
import com.pluu.webtoon.utils.safeNavigate
import com.pluu.webtoon.weekly.model.UI_NAV_ITEM
import com.pluu.webtoon.weekly.model.toUiType
import com.pluu.webtoon.weekly.ui.compose.WeeklyUi
import timber.log.Timber

sealed class Screen(val route: String) {
    object Weekly : Screen("weekly")
    object Episode : Screen("episode") {
        const val ARG_TOON: String = "item"
        const val ARG_COLOR: String = "color"
    }

    object Detail : Screen("detail") {
        const val ARG_COLOR: String = "color"
        const val ARG_EPISODE: String = "episode"
    }

    object Setting : Screen("setting")
    object License : Screen("license")
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    session: CurrentSession,
    bundleSaver: (key: String, data: Bundle) -> Unit,
    openBrowser: (url: String) -> Unit,
    openEpisode: (ToonInfoWithFavorite, PalletColor) -> Unit
) {
    var naviItem by rememberMutableStateOf(session.navi.toUiType())

    NavHost(
        navController = navController,
        startDestination = Screen.Weekly.route,
        modifier = modifier
    ) {
        installWeeklyScreen(navController, naviItem, openEpisode)
        installSettingScreen(navController)
        installLicenseScreen(navController, openBrowser)
    }

    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            Timber.tag("Logger").d("[Destination] ${destination.route}")
        }
        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
}

private fun NavGraphBuilder.installWeeklyScreen(
    navController: NavHostController,
    naviItem: UI_NAV_ITEM,
    openEpisode: (ToonInfoWithFavorite, PalletColor) -> Unit
) {
    composable(Screen.Weekly.route) {
        WeeklyUi(
            naviItem = naviItem,
            onNavigateToMenu = {
                // TODO: reopen webtoon
//                    navController.navigate(route = "Episode")
            },
            openEpisode = { item, color ->
                openEpisode(item, color)
            },
            openSetting = {
                navController.safeNavigate(Screen.Setting.route)
            }
        )
    }
}

private fun NavGraphBuilder.installSettingScreen(
    navController: NavHostController
) {
    composable(Screen.Setting.route) {
        SettingsUi(
            closeCurrent = navController::navigateUp,
            openLicense = {
                navController.safeNavigate(Screen.License.route)
            }
        )
    }
}

private fun NavGraphBuilder.installLicenseScreen(
    navController: NavHostController,
    openBrowser: (url: String) -> Unit
) {
    composable(Screen.License.route) {
        LicenseUi(
            closeCurrent = navController::navigateUp,
            openBrowser = { url ->
                openBrowser(url)
            }
        )
    }
}