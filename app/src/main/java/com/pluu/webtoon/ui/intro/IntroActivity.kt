package com.pluu.webtoon.ui.intro

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pluu.webtoon.R
import com.pluu.webtoon.databinding.ActivityIntroBinding
import com.pluu.webtoon.ui.weekly.MainActivity
import com.pluu.webtoon.utils.observeNonNull
import com.pluu.webtoon.utils.viewbinding.viewBinding
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * 인트로 화면 Activity
 * Created by pluu on 2017-05-07.
 */
class IntroActivity : AppCompatActivity(R.layout.activity_intro) {

    private val viewModel: IntroViewModel by viewModel()
    private val binding by viewBinding(ActivityIntroBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.observe.observeNonNull(this, ::nextView)
    }

    private fun nextView() {
        Timber.i("Login Process Complete")
        binding.tvMsg.setText(R.string.msg_intro_complete)
        binding.progressBar.visibility = View.INVISIBLE

        startActivity(Intent(this@IntroActivity, MainActivity::class.java))
        finish()
    }
}
