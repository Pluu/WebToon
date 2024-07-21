package com.pluu.webtoon.main.container.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.pluu.utils.extraNotNullSerializable
import com.pluu.webtoon.Const
import com.pluu.webtoon.detail.ui.compose.DetailUi
import com.pluu.webtoon.episode.ui.compose.EpisodeUi
import com.pluu.webtoon.main.container.navigator.customtabs.chromeCustomTabs
import com.pluu.webtoon.main.container.navigator.customtabs.navigateChromeCustomTabs
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.setting.ui.LicenseUi
import com.pluu.webtoon.setting.ui.SettingsUi
import com.pluu.webtoon.ui.model.PalletColor
import com.pluu.webtoon.weekly.model.UI_NAV_ITEM
import com.pluu.webtoon.weekly.ui.weekly.WeeklyUi
import kotlinx.serialization.Serializable
import timber.log.Timber

@Serializable
sealed class Screen(val route: String) {
    @Serializable
    data object Weekly : Screen("weekly")
    data object Episode : Screen("episode")
    data object Detail : Screen("detail")

    @Serializable
    data object Setting : Screen("setting")

    @Serializable
    data object License : Screen("license")
}

@Composable
internal fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    naviItem: UI_NAV_ITEM,
    themeColor: Color = MaterialTheme.colorScheme.primary,
    updateNaviItem: (UI_NAV_ITEM) -> Unit,
    updateTheme: (Boolean) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Weekly,
        modifier = modifier,
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        }
    ) {
        installWeeklyScreen(navController, naviItem, updateNaviItem)
        installEpisodeScreen(navController)
        installDetailScreen(navController)
        installSettingScreen(navController)
        installLicenseScreen(navController, themeColor)
        chromeCustomTabs()
    }

    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            Timber.tag("Logger").d("[Destination] ${destination.route}")
            updateTheme(
                when (destination.route) {
                    Screen.Setting::class.java.canonicalName,
                    Screen.License::class.java.canonicalName -> false

                    else -> true
                }
            )
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
    composable<Screen.Weekly> {
        WeeklyUi(
            naviItem = naviItem,
            onNavigateToMenu = { item ->
                updateNaviItem(item)
            },
            openEpisode = { item, color ->
                val toonItem = ToonInfoWithFavorite.toNavigationValue(item)
                val palletColor = PalletColor.toNavigationValue(color)

                navController.navigate(
                    "${Screen.Episode.route}/${toonItem}/${palletColor}"
                )
            },
            openSetting = {
                navController.navigate(Screen.Setting)
            }
        )
    }
}

private fun NavGraphBuilder.installEpisodeScreen(
    navController: NavController
) {
    composable(
        route = Screen.Episode.route + "/{${Const.EXTRA_TOON}}/{${Const.EXTRA_PALLET}}",
        arguments = listOf(
            navArgument(Const.EXTRA_TOON) {
                type = SerializableType(
                    type = ToonInfoWithFavorite::class.java,
                    parser = ToonInfoWithFavorite::parseNavigationValue
                )
            },
            navArgument(Const.EXTRA_PALLET) {
                type = SerializableType(
                    type = PalletColor::class.java,
                    parser = PalletColor::parseNavigationValue
                )
            }
        )
    ) { entry ->
        // Read, Bundle data
        val arguments = requireNotNull(entry.arguments)
        val toon = arguments.extraNotNullSerializable<ToonInfoWithFavorite>(Const.EXTRA_TOON)
        val color = arguments.extraNotNullSerializable<PalletColor>(Const.EXTRA_PALLET)
        // Navigate
        EpisodeUi(
            webToonItem = toon,
            palletColor = color,
            openDetail = { episode ->
                val episodeItem = EpisodeInfo.toNavigationValue(episode)
                val palletColor = PalletColor.toNavigationValue(color)
                navController.navigate("${Screen.Detail.route}/${episodeItem}/${palletColor}")
            },
            closeCurrent = navController::navigateUp
        )
    }
}

private fun NavGraphBuilder.installDetailScreen(
    navController: NavController
) {
    composable(
        route = Screen.Detail.route + "/{${Const.EXTRA_EPISODE}}/{${Const.EXTRA_PALLET}}",
        arguments = listOf(
            navArgument(Const.EXTRA_EPISODE) {
                type = SerializableType(
                    type = EpisodeInfo::class.java,
                    parser = EpisodeInfo::parseNavigationValue
                )
            },
            navArgument(Const.EXTRA_PALLET) {
                type = SerializableType(
                    type = PalletColor::class.java,
                    parser = PalletColor::parseNavigationValue
                )
            }
        )
    ) { entry ->
        // Read, Bundle data
        val color = entry.toRoute<PalletColor>()
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
    composable<Screen.Setting> {
        SettingsUi(
            closeCurrent = navController::navigateUp,
            openLicense = {
                navController.navigate(Screen.License)
            }
        )
    }
}

private fun NavGraphBuilder.installLicenseScreen(
    navController: NavController,
    themeColor: Color
) {
    composable<Screen.License> {
        LicenseUi(
            closeCurrent = navController::navigateUp,
            openBrowser = { url ->
                navController.navigateChromeCustomTabs(
                    url = url,
                    extraBuilder = {
                        setToolbarColor(themeColor.toArgb())
                    }
                )
            }
        )
    }
}