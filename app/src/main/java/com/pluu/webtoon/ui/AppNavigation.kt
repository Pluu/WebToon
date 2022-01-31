package com.pluu.webtoon.ui

import android.os.Bundle
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pluu.compose.runtime.rememberMutableStateOf
import com.pluu.utils.getRequiredSerializableExtra
import com.pluu.webtoon.Const
import com.pluu.webtoon.episode.ui.compose.EpisodeUi
import com.pluu.webtoon.model.CurrentSession
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.setting.ui.LicenseUi
import com.pluu.webtoon.setting.ui.SettingsUi
import com.pluu.webtoon.ui.model.PalletColor
import com.pluu.webtoon.utils.provideLocalSavedHandle
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
    bundleGetter: (key: String) -> Bundle?,
    openBrowser: (url: String) -> Unit,
    openDetail: (EpisodeInfo, PalletColor) -> Unit
) {
    var naviItem by rememberMutableStateOf(session.navi.toUiType())

    NavHost(
        navController = navController,
        startDestination = Screen.Weekly.route,
        modifier = modifier
    ) {
        installWeeklyScreen(navController, naviItem, bundleSaver)
        installEpisodeScreen(navController, bundleSaver, bundleGetter, openDetail)
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
    bundleSaver: (key: String, data: Bundle) -> Unit
) {
    composable(Screen.Weekly.route) {
        WeeklyUi(
            naviItem = naviItem,
            onNavigateToMenu = {
                // TODO: reopen webtoon
//                    navController.navigate(route = "Episode")
            },
            openEpisode = { item, color ->
                // Save, Bundle data
                bundleSaver(
                    Screen.Episode.route,
                    bundleOf(
                        Screen.Episode.ARG_TOON to item,
                        Screen.Episode.ARG_COLOR to color
                    )
                )
                navController.safeNavigate(Screen.Episode.route)
            },
            openSetting = {
                navController.safeNavigate(Screen.Setting.route)
            }
        )
    }
}

private fun NavGraphBuilder.installEpisodeScreen(
    navController: NavHostController,
    bundleSaver: (key: String, data: Bundle) -> Unit,
    bundleGetter: (key: String) -> Bundle?,
    openDetail: (EpisodeInfo, PalletColor) -> Unit
) {
    composable(Screen.Episode.route) {
        // Read, Bundle data
        val arguments = requireNotNull(bundleGetter(Screen.Episode.route))
        val toon: ToonInfoWithFavorite =
            arguments.getRequiredSerializableExtra(Screen.Episode.ARG_TOON)
        val color: PalletColor = arguments.getRequiredSerializableExtra(Screen.Episode.ARG_COLOR)
        // ViewModel에 전달할 SavedHandle 정보
        navController.provideLocalSavedHandle {
            putSerializable(Const.EXTRA_EPISODE, toon)
        }
        // Navigate
        EpisodeUi(
            webToonItem = toon,
            palletColor = color,
            openDetail = { episode ->
                // Save, Bundle data
                bundleSaver(
                    Screen.Detail.route,
                    bundleOf(
                        Screen.Detail.ARG_COLOR to color,
                        Screen.Detail.ARG_EPISODE to episode
                    )
                )
                openDetail(episode, color)
            },
            closeCurrent = navController::navigateUp
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