package com.pluu.webtoon.ui.intro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.pluu.utils.startActivity
import com.pluu.webtoon.ui.compose.activityComposeView
import com.pluu.webtoon.ui.weekly.WeeklyActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * 인트로 화면 Activity
 * Created by pluu on 2017-05-07.
 */
@AndroidEntryPoint
class IntroActivity : ComponentActivity() {

    private val viewModel by viewModels<IntroViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityComposeView {
            val isNextMove by viewModel.observe.observeAsState(null)
            IntroUi(isNextMove != null)

            LaunchedEffect(isNextMove) {
                if (isNextMove != null) {
                    moveMainScreen()
                }
            }
        }
    }

    override fun onBackPressed() {
        // Do Nothing
    }

    private fun moveMainScreen() {
        startActivity<WeeklyActivity>()
        finish()
    }
}
