package com.pluu.webtoon.intro.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.pluu.webtoon.navigator.MainContainerNavigator
import com.pluu.webtoon.ui.compose.WebToonTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * 인트로 화면 Activity
 * Created by pluu on 2017-05-07.
 */
@AndroidEntryPoint
class IntroActivity : ComponentActivity() {
    private val viewModel by viewModels<IntroViewModel>()

    @Inject
    lateinit var mainNavigator: MainContainerNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContent {
            WebToonTheme {
                IntroContent()
            }
        }

        onBackPressedDispatcher.addCallback(this) {
            // Do Nothing
        }
    }

    @Composable
    private fun IntroContent() {
        IntroUi(
            viewModel = viewModel,
            backgroundColor = MaterialTheme.colorScheme.background,
            onNavigateToMain = ::moveMainScreen
        )
    }

    private fun moveMainScreen() {
        mainNavigator.openWebToonContainer(this)
        finish()
    }
}
