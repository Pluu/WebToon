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
import androidx.navigation.toRoute
import com.pluu.webtoon.detail.ui.compose.DetailUi
import com.pluu.webtoon.episode.ui.compose.EpisodeUi
import com.pluu.webtoon.main.container.navigator.customtabs.chromeCustomTabs
import com.pluu.webtoon.main.container.navigator.customtabs.navigateChromeCustomTabs
import com.pluu.webtoon.main.container.utils.navType
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.setting.ui.LicenseUi
import com.pluu.webtoon.setting.ui.SettingsUi
import com.pluu.webtoon.ui.model.PalletColor
import com.pluu.webtoon.weekly.model.UI_NAV_ITEM
import com.pluu.webtoon.weekly.ui.weekly.WeeklyUi
import kotlinx.serialization.Serializable
import timber.log.Timber
import kotlin.reflect.typeOf

@Serializable
sealed interface Screen{
    @Serializable
    data object Weekly : Screen

    @Serializable
    data class Episode(
        val toonInfo: ToonInfoWithFavorite,
        val color: PalletColor
    ) : Screen

    @Serializable
    data class Detail(
        val episode: EpisodeInfo,
        val color: PalletColor
    ) : Screen

    @Serializable
    data object Setting : Screen

    @Serializable
    data object License : Screen
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
                navController.navigate(Screen.Episode(toonInfo = item, color = color))
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
    composable<Screen.Episode>(
        typeMap = mapOf(
            typeOf<ToonInfoWithFavorite>() to navType<ToonInfoWithFavorite>(),
            typeOf<PalletColor>() to navType<PalletColor>(),
        )
    ) { entry ->
        // Read, Bundle data
        val episode = entry.toRoute<Screen.Episode>()
        val toon = episode.toonInfo
        val color = episode.color
        // Navigate
        EpisodeUi(
            webToonItem = toon,
            palletColor = color,
            openDetail = { episode ->
                navController.navigate(Screen.Detail(episode, color))
            },
            closeCurrent = navController::navigateUp
        )
    }
}

private fun NavGraphBuilder.installDetailScreen(
    navController: NavController
) {
    composable<Screen.Detail>(
        typeMap = mapOf(
            typeOf<EpisodeInfo>() to navType<EpisodeInfo>(),
            typeOf<PalletColor>() to navType<PalletColor>(),
        )
    ) { entry ->
        // Read, Bundle data
        val detail = entry.toRoute<Screen.Detail>()
        // Navigate
        DetailUi(
            palletColor = detail.color,
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