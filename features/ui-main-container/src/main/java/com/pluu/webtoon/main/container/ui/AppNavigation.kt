package com.pluu.webtoon.main.container.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.pluu.webtoon.detail.ui.compose.DetailUi
import com.pluu.webtoon.episode.ui.compose.EpisodeUi
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.setting.ui.LicenseUi
import com.pluu.webtoon.setting.ui.SettingsUi
import com.pluu.webtoon.ui.model.PalletColor
import com.pluu.webtoon.weekly.model.UI_NAV_ITEM
import com.pluu.webtoon.weekly.ui.weekly.WeeklyUi
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
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
    naviItem: UI_NAV_ITEM,
    themeColor: Color = MaterialTheme.colorScheme.primary,
    updateNaviItem: (UI_NAV_ITEM) -> Unit,
    updateTheme: (Boolean) -> Unit
) {
    val backStack = remember { mutableStateListOf<Any>(Screen.Weekly) }
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        modifier = modifier,
        entryProvider = entryProvider {
            installWeeklyScreen(
                naviItem = naviItem,
                updateNaviItem = updateNaviItem,
                onOpenEpisode = backStack::add,
                onOpenSetting = {
                    backStack.add(Screen.Setting)
                },
            )
            installEpisodeScreen(
                onBack = {
                    backStack.removeLastOrNull()
                },
                onOpenDetail = backStack::add
            )
            installDetailScreen(
                onBack = {
                    backStack.removeLastOrNull()
                }
            )
            installSettingScreen(
                onBack = {
                    backStack.removeLastOrNull()
                },
                onOpenLicense = {
                    backStack.add(Screen.License)
                }
            )
            installLicenseScreen(
                themeColor = themeColor,
                onBack = {
                    backStack.removeLastOrNull()
                },
                onOpenLicense = {

                },
            )
//            chromeCustomTabs()
        }
    )

    // TODO: 테마 업데이트 대응
//    DisposableEffect(navController) {
//        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
//            Timber.tag("Logger").d("[Destination] ${destination.route}")
//            updateTheme(
//                when (destination.route) {
//                    Screen.Setting::class.java.canonicalName,
//                    Screen.License::class.java.canonicalName -> false
//
//                    else -> true
//                }
//            )
//        }
//        navController.addOnDestinationChangedListener(listener)
//
//        onDispose {
//            navController.removeOnDestinationChangedListener(listener)
//        }
//    }
}

@Composable
private fun EntryProviderScope<Any>.installWeeklyScreen(
    naviItem: UI_NAV_ITEM,
    updateNaviItem: (UI_NAV_ITEM) -> Unit,
    onOpenEpisode: (Screen.Episode) -> Unit,
    onOpenSetting: () -> Unit
) {
    entry<Screen.Weekly> {
        WeeklyUi(
            naviItem = naviItem,
            onNavigateToMenu = { item ->
                updateNaviItem(item)
            },
            openEpisode = { item, color ->
                onOpenEpisode(Screen.Episode(toonInfo = item, color = color))
            },
            openSetting = onOpenSetting
        )
    }
}

private fun EntryProviderScope<Any>.installEpisodeScreen(
    onBack: () -> Unit,
    onOpenDetail: (Screen.Detail) -> Unit
) {
    entry<Screen.Episode> { episode ->
        val toon = episode.toonInfo
        val color = episode.color
        // Navigate
        EpisodeUi(
            webToonItem = toon,
            palletColor = color,
            openDetail = { episode ->
                onOpenDetail(Screen.Detail(episode, color))
            },
            closeCurrent = onBack
        )
    }
}

private fun EntryProviderScope<Any>.installDetailScreen(
    onBack: () -> Unit
) {
    entry<Screen.Detail> { detail ->
        DetailUi(
            episodeInfo = detail.episode,
            palletColor = detail.color,
            closeCurrent = onBack
        )
    }
}

private fun EntryProviderScope<Any>.installSettingScreen(
    onBack: () -> Unit,
    onOpenLicense: () -> Unit
) {
    entry<Screen.Setting> {
        SettingsUi(
            closeCurrent = onBack,
            openLicense = onOpenLicense
        )
    }
}

private fun EntryProviderScope<Any>.installLicenseScreen(
    themeColor: Color,
    onBack: () -> Unit,
    onOpenLicense: () -> Unit
) {
    entry<Screen.License> {
        LicenseUi(
            closeCurrent = onBack,
            openBrowser = { url ->
//                navController.navigateChromeCustomTabs(
//                    url = url,
//                    extraBuilder = {
//                        setToolbarColor(themeColor.toArgb())
//                    }
//                )
            }
        )
    }
}