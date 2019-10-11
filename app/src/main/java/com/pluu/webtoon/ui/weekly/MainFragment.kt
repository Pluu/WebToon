package com.pluu.webtoon.ui.weekly

import android.animation.AnimatorSet
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.pluu.event.EventBus
import com.pluu.support.impl.NaviColorProvider
import com.pluu.webtoon.R
import com.pluu.webtoon.adapter.MainFragmentAdapter
import com.pluu.webtoon.di.UseCaseProperties
import com.pluu.webtoon.domain.base.AbstractWeekApi
import com.pluu.webtoon.event.MainEpisodeLoadedEvent
import com.pluu.webtoon.event.MainEpisodeStartEvent
import com.pluu.webtoon.event.ThemeEvent
import com.pluu.webtoon.utils.animatorStatusBarColor
import com.pluu.webtoon.utils.animatorToolbarColor
import com.pluu.webtoon.utils.lazyNone
import kotlinx.android.synthetic.main.fragment_toon.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named

/**
 * Main View Fragment
 * Created by pluu on 2017-05-07.
 */
class MainFragment : Fragment() {

    private val TAG = MainFragment::class.java.simpleName

    private var isFirstDlg = true

    private val loadDlg: Dialog by lazyNone {
        ProgressDialog(activity).apply {
            setCancelable(false)
            setMessage(getString(R.string.msg_loading))
        }
    }

    private val serviceApi: AbstractWeekApi by inject(named(UseCaseProperties.WEEKLY_USECASE))
    private val colorProvider: NaviColorProvider by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_toon, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setServiceTheme()

        viewPager.apply {
            adapter = MainFragmentAdapter(parentFragmentManager, serviceApi)
            // 금일 기준으로 ViewPager 기본 표시
            currentItem = serviceApi.todayTabPosition
        }

        slidingTabLayout.setupWithViewPager(viewPager)
    }

    // 선택한 서비스에 맞는 컬러 테마 변경
    @ExperimentalCoroutinesApi
    private fun setServiceTheme() {
        val color = colorProvider.getTitleColor()
        val colorDark = colorProvider.getTitleColorDark()
        val activity = activity

        if (activity is AppCompatActivity) {
            val animator1 = activity.animatorToolbarColor(color)
            val animator2 = activity.animatorStatusBarColor(colorDark)

            AnimatorSet().apply {
                duration = 250L
                playTogether(animator1, animator2)
            }.start()
        }

        EventBus.send(ThemeEvent(color, colorDark))
        slidingTabLayout?.setSelectedTabIndicatorColor(color)
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycleScope.launchWhenStarted {
            registerStartEvent()
        }
        lifecycleScope.launchWhenStarted {
            registerLoadEvent()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == WebtoonListFragment.REQUEST_DETAIL_REFERRER) {
            // 포함되어있는 ViewPager 의 Fragment 갱신 처리
            (viewPager.adapter as MainFragmentAdapter).onActivityResult(
                requestCode,
                resultCode,
                data!!
            )
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    private suspend fun registerLoadEvent() {
        EventBus.subscribeToEvent<MainEpisodeLoadedEvent>()
            .collect { eventLoadedEvent() }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    private suspend fun registerStartEvent() {
        EventBus.subscribeToEvent<MainEpisodeStartEvent>()
            .collect { eventStartEvent() }
    }

    private fun eventStartEvent() {
        if (isFirstDlg) {
            Log.d(TAG, "eventStartEvent")
            loadDlg.show()
            isFirstDlg = false
        }
    }

    private fun eventLoadedEvent() {
        if (!isFirstDlg) {
            Log.d(TAG, "eventLoadedEvent")
            loadDlg.dismiss()
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}
