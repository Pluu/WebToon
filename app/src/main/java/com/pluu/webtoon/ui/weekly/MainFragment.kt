package com.pluu.webtoon.ui.weekly

import android.animation.AnimatorSet
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.google.android.material.tabs.TabLayoutMediator
import com.pluu.support.impl.NaviColorProvider
import com.pluu.utils.viewbinding.viewBinding
import com.pluu.webtoon.R
import com.pluu.webtoon.databinding.FragmentToonBinding
import com.pluu.webtoon.domain.usecase.WeeklyUseCase
import com.pluu.webtoon.event.ThemeEvent
import com.pluu.webtoon.utils.ProgressDialog
import com.pluu.webtoon.utils.animatorStatusBarColor
import com.pluu.webtoon.utils.animatorToolbarColor
import com.pluu.webtoon.utils.lazyNone
import com.pluu.webtoon.weekly.WebtoonListFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

/**
 * Main View Fragment
 * Created by pluu on 2017-05-07.
 */
@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_toon) {

    private var isFirstDlg = true

    private val loadDlg: Dialog by lazyNone {
        ProgressDialog.create(requireContext(), R.string.msg_loading).apply {
            setCancelable(false)
        }
    }

    @Inject
    lateinit var serviceApi: WeeklyUseCase

    @Inject
    lateinit var colorProvider: NaviColorProvider

    private val binding by viewBinding(FragmentToonBinding::bind)

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

        registerStartEvent()
        registerLoadEvent()
    }

    // 선택한 서비스에 맞는 컬러 테마 변경
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

        setFragmentResult(
            resultRequestTheme, bundleOf(
                KEY_COLOR to ThemeEvent(color, colorVariant)
            )
        )
        binding.slidingTabLayout.setSelectedTabIndicatorColor(color)
    }

    private fun registerLoadEvent() {
        setFragmentResultListener(WebtoonListFragment.resultRequestEpisodeLoaded) { _, _ ->
            eventLoadedEvent()
        }
    }

    private fun registerStartEvent() {
        setFragmentResultListener(WebtoonListFragment.resultRequestEpisodeStart) { _, _ ->
            eventStartEvent()
        }
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
        const val resultRequestTheme = "resultRequestTheme"
        const val KEY_COLOR = "color"

        fun newInstance() = MainFragment()
    }
}
