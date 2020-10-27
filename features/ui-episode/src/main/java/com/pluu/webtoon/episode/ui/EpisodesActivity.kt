package com.pluu.webtoon.episode.ui

import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import com.pluu.core.utils.lazyNone
import com.pluu.utils.ThemeHelper
import com.pluu.utils.animatorColor
import com.pluu.utils.getRequiredParcelableExtra
import com.pluu.utils.getRequiredSerializableExtra
import com.pluu.utils.getThemeColor
import com.pluu.utils.setStatusBarColor
import com.pluu.utils.viewbinding.viewBinding
import com.pluu.webtoon.Const
import com.pluu.webtoon.episode.R
import com.pluu.webtoon.episode.databinding.ActivityEpisodeBinding
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.ui.model.PalletColor
import dagger.hilt.android.AndroidEntryPoint

/**
 * 에피소드 리스트 Activity
 * Created by pluu on 2017-05-09.
 */
@AndroidEntryPoint
class EpisodesActivity : AppCompatActivity(R.layout.activity_episode) {
    private var childTitle: View? = null

    private val binding by viewBinding(ActivityEpisodeBinding::bind)

    private val webToonItem by lazyNone {
        intent.getRequiredSerializableExtra<ToonInfoWithFavorite>(Const.EXTRA_EPISODE)
    }
    private val palletColor by lazyNone {
        intent.getRequiredParcelableExtra<PalletColor>(Const.EXTRA_PALLET)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)

        initSupportActionBar()
        initView()
        initFragment()
    }

    private fun initSupportActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = webToonItem.info.title
        }
    }

    private fun initView() {
        val toolbar = binding.toolbar
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

        val info = webToonItem.info
        binding.tvName.text = info.writer
        binding.tvRate.text = "평점 : %.2f".format(info.rate)
        binding.tvRate.isVisible = info.rate != 0.0
        binding.btnFirst.setOnClickListener {
            supportFragmentManager.setFragmentResult(EpisodeConst.ShowFirst, Bundle())
        }
    }

    private fun initFragment() {
        val fragment = EpisodeFragment.newInstance(webToonItem, palletColor)

        supportFragmentManager.commit {
            replace(R.id.container, fragment, Const.MAIN_FRAG_TAG)
        }
    }
}
