package com.pluu.webtoon.ui.weekly

import android.os.Bundle
import androidx.fragment.app.commit
import com.pluu.utils.result.setFragmentResultListener
import com.pluu.utils.viewbinding.viewBinding
import com.pluu.webtoon.AppNavigator
import com.pluu.webtoon.Const
import com.pluu.webtoon.R
import com.pluu.webtoon.databinding.ActivityMainBinding
import com.pluu.webtoon.di.provider.NaviColorProvider
import com.pluu.webtoon.event.ThemeEvent
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
    @Inject
    lateinit var appNavigator: AppNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

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
            appNavigator.openSetting(this)
            closeNavDrawer()
        }

        registerThemeChangeEvent()
    }

    private fun registerThemeChangeEvent() {
        setFragmentResultListener(Const.resultTheme) { _, data ->
            themeChange(data.getSerializable(MainFragment.KEY_COLOR) as ThemeEvent)
        }
    }

    private fun themeChange(event: ThemeEvent) {
        binding.navDrawer.navTitle.setBackgroundColor(event.variantColor)
    }
}
