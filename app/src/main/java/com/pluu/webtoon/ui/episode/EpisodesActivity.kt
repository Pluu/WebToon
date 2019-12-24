package com.pluu.webtoon.ui.episode

import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import com.pluu.event.EventBus
import com.pluu.webtoon.R
import com.pluu.webtoon.common.Const
import com.pluu.webtoon.databinding.ActivityEpisodeBinding
import com.pluu.webtoon.domain.moel.ToonInfo
import com.pluu.webtoon.event.FirstItemSelectEvent
import com.pluu.webtoon.utils.RippleDrawableFactory
import com.pluu.webtoon.utils.animatorToolbarColor
import com.pluu.webtoon.utils.lazyNone
import com.pluu.webtoon.utils.setStatusBarColor
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * 에피소드 리스트 Activity
 * Created by pluu on 2017-05-09.
 */
class EpisodesActivity : AppCompatActivity() {
    private var childTitle: View? = null

    private lateinit var binding: ActivityEpisodeBinding

    private val webToonInfo by lazyNone {
        intent.getParcelableExtra<ToonInfo>(Const.EXTRA_EPISODE)!!
    }
    private var customTitleColor: Int = 0
    private var customStatusColor: Int = 0

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEpisodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.actionBar.toolbarActionbar)

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
        customTitleColor = intent.getIntExtra(Const.EXTRA_MAIN_COLOR, Color.BLACK)
        customStatusColor = intent.getIntExtra(Const.EXTRA_STATUS_COLOR, Color.BLACK)

        // Title TextView
        for (i in 0 until binding.actionBar.toolbarActionbar.childCount) {
            if (binding.actionBar.toolbarActionbar.getChildAt(i) is TextView) {
                childTitle = binding.actionBar.toolbarActionbar.getChildAt(i)
                break
            }
        }

        val listener = ValueAnimator.AnimatorUpdateListener { animation ->
            val value = animation.animatedValue as Int
            binding.actionBar.toolbarActionbar.setBackgroundColor(value)
            binding.btnFirst.setBackgroundColor(value)
            childTitle?.setBackgroundColor(value)
            binding.tvName.setTextColor(value)
            binding.tvRate.setTextColor(value)

            this@EpisodesActivity.setStatusBarColor(value)
        }

        animatorToolbarColor(customTitleColor, listener).let {
            it.duration = 1000L
            it.doOnEnd {
                binding.btnFirst.background =
                    RippleDrawableFactory.createFromColor(
                        Color.WHITE,
                        ColorDrawable(customTitleColor)
                    )
            }
            it.start()
        }

        binding.tvName.text = webToonInfo.writer
        binding.tvRate.text = Const.getRateNameByRate(webToonInfo.rate)
        binding.tvRate.isVisible = webToonInfo.rate != 0.0

        binding.btnFirst.setOnClickListener {
            EventBus.send(FirstItemSelectEvent)
        }
    }

    private fun initFragment() {
        val fragment = EpisodeFragment.newInstance(
            webToonInfo,
            intArrayOf(customTitleColor, customStatusColor)
        )

        supportFragmentManager.commit {
            replace(R.id.container, fragment, Const.MAIN_FRAG_TAG)
        }
    }
}
