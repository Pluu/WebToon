package com.pluu.webtoon.ui.intro

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.pluu.utils.observeNonNull
import com.pluu.utils.startActivity
import com.pluu.utils.viewbinding.viewBinding
import com.pluu.webtoon.R
import com.pluu.webtoon.databinding.ActivityIntroBinding
import com.pluu.webtoon.ui.weekly.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

/**
 * 인트로 화면 Activity
 * Created by pluu on 2017-05-07.
 */
@AndroidEntryPoint
class IntroActivity : AppCompatActivity(R.layout.activity_intro) {

    private val viewModel by viewModels<IntroViewModel>()
    private val binding by viewBinding(ActivityIntroBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.observe.observeNonNull(this, ::nextView)
    }

    private fun nextView() {
        Timber.i("Login Process Complete")
        binding.tvMsg.setText(R.string.msg_intro_complete)
        binding.progressBar.visibility = View.INVISIBLE

        startActivity<MainActivity>()
        finish()
    }
}
