package com.pluu.webtoon.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.pluu.event.RxBusProvider
import com.pluu.support.impl.NAV_ITEM
import com.pluu.webtoon.R
import com.pluu.webtoon.common.Const
import com.pluu.webtoon.common.PrefConfig
import com.pluu.webtoon.event.ThemeEvent
import com.pluu.webtoon.ui.settting.SettingsActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.navdrawer.*

/**
 * 메인 화면 Activity
 * Created by pluu on 2017-05-07.
 */
class MainActivity : BaseNavActivity(), MainFragment.BindServiceListener {

    private var mCompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }

        selfNavDrawerItem = PrefConfig.getDefaultWebToon(this)

        themeChange(ThemeEvent(ContextCompat.getColor(this, selfNavDrawerItem.color),
                ContextCompat.getColor(this, selfNavDrawerItem.bgColor)))

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, MainFragment.newInstance(selfNavDrawerItem), Const.MAIN_FRAG_TAG)
                .commit()

        btnSetting.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            closeNavDrawer()
        }
    }

    override fun onResume() {
        super.onResume()
        mCompositeDisposable.add(
                RxBusProvider.getInstance()
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
            supportFragmentManager.findFragmentByTag(Const.MAIN_FRAG_TAG)?.
                    onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun bindNavItem(item: NAV_ITEM) {
        selfNavDrawerItem = item
    }
}
