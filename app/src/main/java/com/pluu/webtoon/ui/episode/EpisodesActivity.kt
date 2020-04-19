package com.pluu.webtoon.ui.episode

import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import com.pluu.event.EventBus
import com.pluu.webtoon.R
import com.pluu.webtoon.common.Const
import com.pluu.webtoon.databinding.ActivityEpisodeBinding
import com.pluu.webtoon.domain.moel.ToonInfo
import com.pluu.webtoon.event.FirstItemSelectEvent
import com.pluu.webtoon.ui.weekly.PalletColor
import com.pluu.webtoon.utils.ThemeHelper
import com.pluu.webtoon.utils.animatorColor
import com.pluu.webtoon.utils.getRequiredParcelableExtra
import com.pluu.webtoon.utils.getThemeColor
import com.pluu.webtoon.utils.lazyNone
import com.pluu.webtoon.utils.setStatusBarColor
import com.pluu.webtoon.utils.viewbinding.viewBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * 에피소드 리스트 Activity
 * Created by pluu on 2017-05-09.
 */
class EpisodesActivity : AppCompatActivity(R.layout.activity_episode) {
    private var childTitle: View? = null

    private val binding by viewBinding(ActivityEpisodeBinding::bind)

    private val webToonInfo by lazyNone {
        intent.getRequiredParcelableExtra<ToonInfo>(Const.EXTRA_EPISODE)
    }
    private val palletColor by lazyNone {
        intent.getRequiredParcelableExtra<PalletColor>(Const.EXTRA_PALLET)
    }

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.actionBar.toolbar)

        initSupportActionBar()
        initView()
        initFragment()
    }

    private fun initSupportActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = webToonInfo.title
        }
    }

    @ExperimentalCoroutinesApi
    private fun initView() {
        val toolbar = binding.actionBar.toolbar
        // Title TextView
        for (i in 0 until toolbar.childCount) {
            if (toolbar.getChildAt(i) is TextView) {
                childTitle = toolbar.getChildAt(i)
                break
            }
        }

        val variantListener = ValueAnimator.AnimatorUpdateListener { animation ->
            val value = animation.animatedValue as Int
            toolbar.setBackgroundColor(value)
            binding.btnFirst.backgroundTintList = ColorStateList.valueOf(value)
            childTitle?.setBackgroundColor(value)

            this@EpisodesActivity.setStatusBarColor(value)
        }
        animatorColor(
            startColor = getThemeColor(R.attr.colorPrimary),
            endColor = palletColor.darkVibrantColor,
            listener = variantListener
        ).apply {
            duration = 1000L
        }.start()

        val writerListener = ValueAnimator.AnimatorUpdateListener { animation ->
            val value = animation.animatedValue as Int
            binding.tvName.setTextColor(value)
            binding.tvRate.setTextColor(value)
        }
        animatorColor(
            startColor = getThemeColor(android.R.attr.textColorPrimary),
            endColor = if (ThemeHelper.isLightTheme(this)) palletColor.darkMutedColor else palletColor.lightMutedColor,
            listener = writerListener
        ).apply {
            duration = 1000L
        }.start()

        binding.tvName.text = webToonInfo.writer
        binding.tvRate.text = Const.getRateNameByRate(webToonInfo.rate)
        binding.tvRate.isVisible = webToonInfo.rate != 0.0

        binding.btnFirst.setOnClickListener {
            EventBus.send(FirstItemSelectEvent)
        }
    }

    private fun initFragment() {
        val fragment = EpisodeFragment.newInstance(webToonInfo, palletColor)

        supportFragmentManager.commit {
            replace(R.id.container, fragment, Const.MAIN_FRAG_TAG)
        }
    }
}
