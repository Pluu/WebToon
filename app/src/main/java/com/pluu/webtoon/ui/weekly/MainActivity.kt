package com.pluu.webtoon.ui.weekly

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.pluu.event.EventBus
import com.pluu.support.impl.NaviColorProvider
import com.pluu.webtoon.R
import com.pluu.webtoon.common.Const
import com.pluu.webtoon.databinding.ActivityMainBinding
import com.pluu.webtoon.event.ThemeEvent
import com.pluu.webtoon.ui.settting.SettingsActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject

/**
 * 메인 화면 Activity
 * Created by pluu on 2017-05-07.
 */
class MainActivity : BaseNavActivity() {

    private lateinit var binding: ActivityMainBinding
    private val defaultProvider: NaviColorProvider by inject()

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.actionBar.toolbarActionbar)

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

        supportFragmentManager.commit {
            replace(
                R.id.container, MainFragment.newInstance(), Const.MAIN_FRAG_TAG
            )
        }
        binding.navDrawer.btnSetting.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            closeNavDrawer()
        }

        lifecycleScope.launchWhenStarted {
            registerThemeChangeEvent()
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    private suspend fun registerThemeChangeEvent() {
        EventBus.subscribeToEvent<ThemeEvent>()
            .collect { themeChange(it) }
    }

    private fun themeChange(event: ThemeEvent) {
        binding.navDrawer.navTitle.setBackgroundColor(event.darkColor)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            supportFragmentManager.findFragmentByTag(Const.MAIN_FRAG_TAG)
                ?.onActivityResult(requestCode, resultCode, data)
        }
    }
}
