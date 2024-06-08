package com.pluu.webtoon.intro.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
    @Inject
    lateinit var mainNavigator: MainContainerNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        setContent {
            WebToonTheme {
                IntroContent()
            }
        }
    }

    @Composable
    private fun IntroContent() {
        BackHandler {
            // Do Nothing
        }
        IntroScreen(
            backgroundColor = MaterialTheme.colorScheme.background,
            onNavigateToMain = ::moveMainScreen
        )
    }

    private fun moveMainScreen() {
        mainNavigator.openWebToonContainer(this)
        finish()
    }
}
