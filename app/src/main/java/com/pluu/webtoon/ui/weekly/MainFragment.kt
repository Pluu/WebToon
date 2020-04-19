package com.pluu.webtoon.ui.weekly

import android.animation.AnimatorSet
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.pluu.event.EventBus
import com.pluu.support.impl.NaviColorProvider
import com.pluu.webtoon.R
import com.pluu.webtoon.adapter.MainFragmentAdapter
import com.pluu.webtoon.databinding.FragmentToonBinding
import com.pluu.webtoon.di.UseCaseProperties
import com.pluu.webtoon.domain.base.AbstractWeekApi
import com.pluu.webtoon.event.MainEpisodeLoadedEvent
import com.pluu.webtoon.event.MainEpisodeStartEvent
import com.pluu.webtoon.event.ThemeEvent
import com.pluu.webtoon.utils.ProgressDialog
import com.pluu.webtoon.utils.animatorStatusBarColor
import com.pluu.webtoon.utils.animatorToolbarColor
import com.pluu.webtoon.utils.lazyNone
import com.pluu.webtoon.utils.viewbinding.viewBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named
import timber.log.Timber

/**
 * Main View Fragment
 * Created by pluu on 2017-05-07.
 */
class MainFragment : Fragment(R.layout.fragment_toon) {

    private var isFirstDlg = true

    private val loadDlg: Dialog by lazyNone {
        ProgressDialog.create(requireContext(), R.string.msg_loading).apply {
            setCancelable(false)
        }
    }

    private val serviceApi: AbstractWeekApi by inject(named(UseCaseProperties.WEEKLY_USECASE))
    private val colorProvider: NaviColorProvider by inject()

    private val binding by viewBinding(FragmentToonBinding::bind)

    @Suppress("EXPERIMENTAL_API_USAGE")
    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setServiceTheme()

        binding.viewPager.apply {
            adapter = MainFragmentAdapter(
                fm = parentFragmentManager,
                serviceApi = serviceApi,
                lifecycle = viewLifecycleOwner.lifecycle
            )
            // 금일 기준으로 ViewPager 기본 표시
            currentItem = serviceApi.todayTabPosition
        }

        TabLayoutMediator(binding.slidingTabLayout, binding.viewPager) { tab, position ->
            tab.text = serviceApi.getWeeklyTabName(position)
        }.attach()

        lifecycleScope.launchWhenStarted {
            registerStartEvent()
        }
        lifecycleScope.launchWhenStarted {
            registerLoadEvent()
        }
    }

    // 선택한 서비스에 맞는 컬러 테마 변경
    @ExperimentalCoroutinesApi
    private fun setServiceTheme() {
        val color = colorProvider.getTitleColor()
        val colorVariant = colorProvider.getTitleColorVariant()
        val activity = activity

        if (activity is AppCompatActivity) {
            val animator1 = activity.animatorToolbarColor(color)
            val animator2 = activity.animatorStatusBarColor(colorVariant)

            AnimatorSet().apply {
                duration = 250L
                playTogether(animator1, animator2)
            }.start()
        }

        EventBus.send(ThemeEvent(color, colorVariant))
        binding.slidingTabLayout.setSelectedTabIndicatorColor(color)
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
            Timber.d("eventStartEvent")
            loadDlg.show()
            isFirstDlg = false
        }
    }

    private fun eventLoadedEvent() {
        if (!isFirstDlg) {
            Timber.d("eventLoadedEvent")
            loadDlg.dismiss()
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}
