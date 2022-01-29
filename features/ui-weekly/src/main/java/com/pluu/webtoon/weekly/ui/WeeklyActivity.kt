package com.pluu.webtoon.weekly.ui

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pluu.compose.runtime.rememberMutableStateOf
import com.pluu.webtoon.Const
import com.pluu.webtoon.model.CurrentSession
import com.pluu.webtoon.navigator.SettingNavigator
import com.pluu.webtoon.ui.compose.activityComposeView
import com.pluu.webtoon.weekly.event.WeeklyMenuEvent
import com.pluu.webtoon.weekly.model.getCoreType
import com.pluu.webtoon.weekly.model.toUiType
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

/**
 * 주간 화면 Activity
 * Created by pluu on 2017-05-07.
 */
@AndroidEntryPoint
class WeeklyActivity : FragmentActivity() {

    @Inject
    lateinit var session: CurrentSession

    @Inject
    lateinit var settingNavigator: SettingNavigator

    private val containerViewId by lazy {
        View.generateViewId()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        activityComposeView {
            val systemUiController = rememberSystemUiController()
            SideEffect {
                systemUiController.setSystemBarsColor(Color.Transparent)
            }

            ProvideWindowInsets(false) {
                var naviItem by rememberMutableStateOf(session.navi.toUiType())

                val contentView = remember {
                    FragmentContainerView(this).apply {
                        id = containerViewId
                        replaceMainContainer(WeeklyContainerFragment.newInstance())
                    }
                }

                WeeklyScreen(
                    naviItem = naviItem,
                    onEventAction = { event ->
                        when (event) {
                            is WeeklyMenuEvent.OnMenuClicked -> {
                                if (naviItem != event.item) {
                                    Timber.d(event.item.name)
                                    session.navi = event.item.getCoreType()
                                    naviItem = event.item
                                    replaceMainContainer(WeeklyContainerFragment.newInstance())
                                }
                            }
                            WeeklyMenuEvent.OnSettingClicked -> {
                                settingNavigator.openSetting(this)
                            }
                        }
                    }
                ) { innerPadding ->
                    AndroidView(
                        modifier = Modifier.padding(innerPadding),
                        factory = { contentView }
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
