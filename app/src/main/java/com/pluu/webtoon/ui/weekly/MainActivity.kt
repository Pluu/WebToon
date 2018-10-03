package com.pluu.webtoon.ui.weekly

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.pluu.event.RxBusProvider
import com.pluu.support.impl.NaviColorProvider
import com.pluu.webtoon.R
import com.pluu.webtoon.common.Const
import com.pluu.webtoon.event.ThemeEvent
import com.pluu.webtoon.ui.settting.SettingsActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.navdrawer.*
import org.koin.android.ext.android.inject

/**
 * 메인 화면 Activity
 * Created by pluu on 2017-05-07.
 */
class MainActivity : BaseNavActivity() {

    private var mCompositeDisposable = CompositeDisposable()

    private val defaultProvider: NaviColorProvider by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }

        themeChange(
            ThemeEvent(
                defaultProvider.getTitleColor(),
                defaultProvider.getTitleColorDark()
            )
        )

        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.container,
                MainFragment.newInstance(),
                Const.MAIN_FRAG_TAG
            )
            .commit()

        btnSetting.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            closeNavDrawer()
        }
    }

    override fun onResume() {
        super.onResume()
        mCompositeDisposable.add(
            RxBusProvider.instance
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(busEvent)
        )
    }

    override fun onPause() {
        super.onPause()
        mCompositeDisposable.clear()
    }

    private val busEvent = Consumer<Any> {
        when (it) {
            is ThemeEvent -> themeChange(it)
        }
    }

    private fun themeChange(event: ThemeEvent) {
        navTitle.setBackgroundColor(event.darkColor)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            supportFragmentManager.findFragmentByTag(Const.MAIN_FRAG_TAG)
                ?.onActivityResult(requestCode, resultCode, data)
        }
    }
}
