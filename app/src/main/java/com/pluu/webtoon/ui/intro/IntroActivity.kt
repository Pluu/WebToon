package com.pluu.webtoon.ui.intro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.onCommit
import com.pluu.utils.startActivity
import com.pluu.webtoon.ui.compose.ActivityComposeView
import com.pluu.webtoon.ui.weekly.MainActivity
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

        ActivityComposeView {
            val isNextMove by viewModel.observe.observeAsState()
            IntroUi(isNextMove != null)

            onCommit(isNextMove) {
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
        startActivity<MainActivity>()
        finish()
    }
}
