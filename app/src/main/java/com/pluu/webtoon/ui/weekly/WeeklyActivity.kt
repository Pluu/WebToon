package com.pluu.webtoon.ui.weekly

import android.os.Bundle
import android.view.View
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import com.pluu.compose.ui.graphics.toColor
import com.pluu.webtoon.Const
import com.pluu.webtoon.R
import com.pluu.webtoon.model.Session
import com.pluu.webtoon.model.UI_NAV_ITEM
import com.pluu.webtoon.model.getCoreType
import com.pluu.webtoon.model.toUiType
import com.pluu.webtoon.navigator.SettingNavigator
import com.pluu.webtoon.ui.compose.activityComposeView
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import timber.log.Timber
import javax.inject.Inject

/**
 * 주간 화면 Activity
 * Created by pluu on 2017-05-07.
 */
@AndroidEntryPoint
class WeeklyActivity : FragmentActivity() {

    @Inject
    lateinit var session: Session

    @Inject
    lateinit var settingNavigator: SettingNavigator

    private val containerViewId by lazy {
        View.generateViewId()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        activityComposeView {
            ProvideWindowInsets(false) {
                val context = LocalContext.current
                var naviItem by remember { mutableStateOf(session.navi.toUiType()) }

                val contentView = remember {
                    FragmentContainerView(this).apply {
                        id = containerViewId
                        replaceMainContainer(WeeklyContainerFragment.newInstance())
                    }
                }

                WeeklyContainerUi(
                    naviItem = naviItem,
                    backgroundColor = ContextCompat.getColor(context, naviItem.bgColor)
                        .toColor(),
                    changeNavi = { newNaviItem ->
                        Timber.d(newNaviItem.name)
                        session.navi = newNaviItem.getCoreType()
                        naviItem = newNaviItem
                        replaceMainContainer(WeeklyContainerFragment.newInstance())
                    },
                    onSettingClicked = {
                        settingNavigator.openSetting(this)
                    }
                ) { innerPadding ->
                    AndroidView(
                        modifier = Modifier.padding(innerPadding),
                        viewBlock = { contentView }
                    )
                }
            }
        }
    }

    private fun replaceMainContainer(fragment: Fragment) {
        supportFragmentManager.commit {
            supportFragmentManager.findFragmentByTag(Const.MAIN_FRAG_TAG)?.let {
                remove(it)
            }
            replace(containerViewId, fragment, Const.MAIN_FRAG_TAG)
        }
    }
}

@Composable
private fun WeeklyContainerUi(
    naviItem: UI_NAV_ITEM,
    backgroundColor: Color,
    changeNavi: (UI_NAV_ITEM) -> Unit,
    onSettingClicked: () -> Unit,
    bodyContent: @Composable (PaddingValues) -> Unit
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()

    BackHandler(scaffoldState.drawerState.isOpen) {
        scaffoldState.drawerState.close()
    }

    Scaffold(
        drawerContent = {
            WeeklyDrawer(
                title = context.getString(R.string.app_name),
                accentColor = backgroundColor,
                menus = UI_NAV_ITEM.values().iterator(),
                selectedMenu = naviItem,
                onMenuClicked = { item ->
                    if (item != naviItem) {
                        changeNavi(item)
                    }
                    scaffoldState.drawerState.close()
                },
                onSettingClicked = {
                    onSettingClicked()
                    scaffoldState.drawerState.close()
                }
            )
        },
        drawerGesturesEnabled = false,
        drawerElevation = 0.dp,
        drawerScrimColor = MaterialTheme.colors.background.copy(alpha = 0.5f),
        topBar = {
            WeeklyTopBar(
                title = context.getString(R.string.app_name),
                backgroundColor = backgroundColor
            ) {
                scaffoldState.drawerState.open()
            }
        },
        scaffoldState = scaffoldState
    ) { innerPadding ->
        bodyContent(innerPadding)
    }
}