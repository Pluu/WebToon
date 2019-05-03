package com.pluu.webtoon.ui.episode

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.pluu.event.EventBus
import com.pluu.webtoon.R
import com.pluu.webtoon.common.Const
import com.pluu.webtoon.event.FirstItemSelectEvent
import com.pluu.webtoon.item.ToonInfo
import com.pluu.webtoon.utils.animatorToolbarColor
import com.pluu.webtoon.utils.lazyNone
import com.pluu.webtoon.utils.setStatusBarColor
import kotlinx.android.synthetic.main.activity_episode.*
import kotlinx.android.synthetic.main.toolbar_actionbar.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi

/**
 * 에피소드 리스트 Activity
 * Created by pluu on 2017-05-09.
 */
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class EpisodesActivity : AppCompatActivity() {
    private var childTitle: View? = null

    private val webToonInfo: ToonInfo by lazyNone {
        intent.getParcelableExtra<ToonInfo>(Const.EXTRA_EPISODE)
    }
    private var customTitleColor: Int = 0
    private var customStatusColor: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_episode)
        setSupportActionBar(toolbar_actionbar)

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

    private fun initView() {
        customTitleColor = intent.getIntExtra(Const.EXTRA_MAIN_COLOR, Color.BLACK)
        customStatusColor = intent.getIntExtra(Const.EXTRA_STATUS_COLOR, Color.BLACK)

        // Title TextView
        for (i in 0 until toolbar_actionbar.childCount) {
            if (toolbar_actionbar.getChildAt(i) is TextView) {
                childTitle = toolbar_actionbar.getChildAt(i)
                break
            }
        }

        val listener = ValueAnimator.AnimatorUpdateListener { animation ->
            val value = animation.animatedValue as Int
            toolbar_actionbar.setBackgroundColor(value)
            btnFirst.setBackgroundColor(value)
            childTitle?.setBackgroundColor(value)
            tvName.setTextColor(value)
            tvRate.setTextColor(value)

            this@EpisodesActivity.setStatusBarColor(value)
        }

        animatorToolbarColor(customTitleColor, listener).let {
            it.duration = 1000L
            it.start()
        }

        tvName.text = webToonInfo.writer
        if (webToonInfo.rate.isNotEmpty()) {
            tvRate.text = webToonInfo.rate
            tvRate.visibility = View.VISIBLE
        }

        btnFirst.setOnClickListener {
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
