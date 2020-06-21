package com.pluu.webtoon.ui.weekly

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.commit
import com.pluu.support.impl.NaviColorProvider
import com.pluu.utils.viewbinding.viewBinding
import com.pluu.webtoon.R
import com.pluu.webtoon.common.Const
import com.pluu.webtoon.databinding.ActivityMainBinding
import com.pluu.webtoon.event.ThemeEvent
import com.pluu.webtoon.ui.settting.SettingsActivity
import com.pluu.webtoon.utils.result.setFragmentResultListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * 메인 화면 Activity
 * Created by pluu on 2017-05-07.
 */
@AndroidEntryPoint
class MainActivity : BaseNavActivity(R.layout.activity_main) {

    private val binding by viewBinding(ActivityMainBinding::bind)
    @Inject
    lateinit var defaultProvider: NaviColorProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.actionBar.toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }

        themeChange(
            ThemeEvent(
                defaultProvider.getTitleColor(),
                defaultProvider.getTitleColorVariant()
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

        registerThemeChangeEvent()
    }

    private fun registerThemeChangeEvent() {
        setFragmentResultListener(
            MainFragment.resultRequestTheme
        ) { _, data ->
            themeChange(data.getSerializable(MainFragment.KEY_COLOR) as ThemeEvent)
        }
    }

    private fun themeChange(event: ThemeEvent) {
        binding.navDrawer.navTitle.setBackgroundColor(event.variantColor)
    }
}
