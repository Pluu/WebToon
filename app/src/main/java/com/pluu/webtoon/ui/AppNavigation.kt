package com.pluu.webtoon.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pluu.utils.getRequiredSerializableExtra
import com.pluu.webtoon.Const
import com.pluu.webtoon.detail.ui.compose.DetailUi
import com.pluu.webtoon.episode.ui.compose.EpisodeUi
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.setting.ui.LicenseUi
import com.pluu.webtoon.setting.ui.SettingsUi
import com.pluu.webtoon.ui.model.PalletColor
import com.pluu.webtoon.utils.navigateWithArgument
import com.pluu.webtoon.weekly.model.UI_NAV_ITEM
import com.pluu.webtoon.weekly.ui.compose.WeeklyUi
import timber.log.Timber

sealed class Screen(val route: String) {
    object Weekly : Screen("weekly")
    object Episode : Screen("episode")
    object Detail : Screen("detail")
    object Setting : Screen("setting")
    object License : Screen("license")
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    naviItem: UI_NAV_ITEM,
    openBrowser: (url: String) -> Unit,
    updateNaviItem: (UI_NAV_ITEM) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Weekly.route,
        modifier = modifier
    ) {
        installWeeklyScreen(navController, naviItem, updateNaviItem)
        installEpisodeScreen(navController)
        installDetailScreen(navController)
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
    navController: NavController,
    naviItem: UI_NAV_ITEM,
    updateNaviItem: (UI_NAV_ITEM) -> Unit
) {
    composable(Screen.Weekly.route) {
        WeeklyUi(
            naviItem = naviItem,
            onNavigateToMenu = { item ->
                updateNaviItem(item)
            },
            openEpisode = { item, color ->
                navController.navigateWithArgument(
                    route = Screen.Episode.route,
                    args = listOf(
                        Const.EXTRA_TOON to item,
                        Const.EXTRA_PALLET to color
                    )
                )
            },
            openSetting = {
                navController.navigateWithArgument(Screen.Setting.route)
            }
        )
    }
}

private fun NavGraphBuilder.installEpisodeScreen(
    navController: NavController
) {
    composable(Screen.Episode.route) { entry ->
        // Read, Bundle data
        val arguments = requireNotNull(entry.arguments)
        val toon: ToonInfoWithFavorite =
            arguments.getRequiredSerializableExtra(Const.EXTRA_TOON)
        val color: PalletColor = arguments.getRequiredSerializableExtra(Const.EXTRA_PALLET)
        // Navigate
        EpisodeUi(
            webToonItem = toon,
            palletColor = color,
            openDetail = { episode ->
                navController.navigateWithArgument(
                    route = Screen.Detail.route,
                    args = listOf(
                        Const.EXTRA_EPISODE to episode,
                        Const.EXTRA_PALLET to color
                    )
                )
            },
            closeCurrent = navController::navigateUp
        )
    }
}

private fun NavGraphBuilder.installDetailScreen(
    navController: NavController
) {
    composable(Screen.Detail.route) { entry ->
        // Read, Bundle data
        val arguments = requireNotNull(entry.arguments)
        val color: PalletColor = arguments.getRequiredSerializableExtra(Const.EXTRA_PALLET)

        // Navigate
        DetailUi(
            palletColor = color,
            closeCurrent = navController::navigateUp
        )
    }
}

private fun NavGraphBuilder.installSettingScreen(
    navController: NavController
) {
    composable(Screen.Setting.route) {
        SettingsUi(
            closeCurrent = navController::navigateUp,
            openLicense = {
                navController.navigateWithArgument(Screen.License.route)
            }
        )
    }
}

private fun NavGraphBuilder.installLicenseScreen(
    navController: NavController,
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