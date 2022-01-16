package com.pluu.webtoon.intro.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.pluu.webtoon.navigator.WeeklyNavigator
import com.pluu.webtoon.ui.compose.activityComposeView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * 인트로 화면 Activity
 * Created by pluu on 2017-05-07.
 */
@AndroidEntryPoint
class IntroActivity : ComponentActivity() {
    private val viewModel by viewModels<IntroViewModel>()

    @Inject
    lateinit var weeklyNavigator: WeeklyNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        activityComposeView {
            val isNextMove by viewModel.observe.collectAsState(false)
            IntroScreen(isLoading = !isNextMove)

            if (isNextMove) {
                LaunchedEffect(true) {
                    delay(500L)
                    moveMainScreen()
                }
            }
        }
    }

    override fun onBackPressed() {
        // Do Nothing
    }

    private fun moveMainScreen() {
        weeklyNavigator.openWeekly(this)
        finish()
    }
}
