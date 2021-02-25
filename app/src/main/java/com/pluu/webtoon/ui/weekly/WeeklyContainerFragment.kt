package com.pluu.webtoon.ui.weekly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.viewpager2.widget.ViewPager2
import com.pluu.compose.ui.graphics.toColor
import com.pluu.webtoon.Const
import com.pluu.webtoon.di.provider.NaviColorProvider
import com.pluu.webtoon.domain.usecase.WeeklyUseCase
import com.pluu.webtoon.event.ThemeEvent
import com.pluu.webtoon.ui.compose.FragmentComposeView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Main View Fragment
 * Created by pluu on 2017-05-07.
 */
@AndroidEntryPoint
class WeeklyContainerFragment : Fragment() {

    @Inject
    lateinit var serviceApi: WeeklyUseCase

    @Inject
    lateinit var colorProvider: NaviColorProvider

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentComposeView {
        val context = LocalContext.current
        var selectedIndex by remember { mutableStateOf(serviceApi.todayTabPosition) }

        val pager = remember {
            ViewPager2(context).apply {
                orientation = ViewPager2.ORIENTATION_HORIZONTAL
                adapter = WeeklyFragmentAdapter(
                    fm = parentFragmentManager,
                    serviceApi = serviceApi,
                    lifecycle = viewLifecycleOwner.lifecycle
                )
                // 금일 기준으로 ViewPager 기본 표시
                setCurrentItem(selectedIndex, false)
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        selectedIndex = position
                    }
                })
            }
        }

        Column {
            DayOfWeekUi(
                titles = serviceApi.CURRENT_TABS,
                selectedTabIndex = selectedIndex,
                indicatorColor = colorProvider.getTitleColor().toColor()
            ) {
                selectedIndex = it
                pager.currentItem = it
            }
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { pager }
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResult(
            Const.resultTheme, bundleOf(
                KEY_COLOR to ThemeEvent(
                    colorProvider.getTitleColor(),
                    colorProvider.getTitleColorVariant()
                )
            )
        )
    }

    companion object {
        const val KEY_COLOR = "color"

        fun newInstance() = WeeklyContainerFragment()
    }
}
