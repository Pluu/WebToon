package com.pluu.webtoon.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.pluu.webtoon.R
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_intro.*
import java.util.concurrent.TimeUnit

/**
 * 인트로 화면 Activity
 * Created by pluu on 2017-05-07.
 */
class IntroActivity : Activity() {
    private val TAG = IntroActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        initWork()
    }

    private fun initWork() {
        Single.fromCallable { "" }
                .delay(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { _ ->
                    Log.i(TAG, "Login Process Complete")
                    tvMsg.setText(R.string.msg_intro_complete)
                    progressBar.visibility = View.INVISIBLE

                    startActivity(Intent(this@IntroActivity, MainActivity::class.java))
                    finish()
                }
    }

}
