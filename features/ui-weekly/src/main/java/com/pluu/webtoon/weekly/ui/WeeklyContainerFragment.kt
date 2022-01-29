package com.pluu.webtoon.weekly.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.google.accompanist.insets.ProvideWindowInsets
import com.pluu.webtoon.Const
import com.pluu.webtoon.domain.usecase.site.GetWebToonTabsUseCase
import com.pluu.webtoon.navigator.EpisodeNavigator
import com.pluu.webtoon.ui.compose.fragmentComposeView
import com.pluu.webtoon.ui.compose.navigator.LocalEpisodeNavigator
import com.pluu.webtoon.weekly.event.ThemeEvent
import com.pluu.webtoon.weekly.provider.NaviColorProvider
import com.pluu.webtoon.weekly.ui.compose.WeeklyContainerScreen
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

/**
 * Main View Fragment
 * Created by pluu on 2017-05-07.
 */
@AndroidEntryPoint
class WeeklyContainerFragment : Fragment() {

    private val todayTabPosition by lazy {
        (Calendar.getInstance(Locale.getDefault()).get(Calendar.DAY_OF_WEEK) + 5) % 7
    }

    @Inject
    lateinit var serviceApi: GetWebToonTabsUseCase

    @Inject
    lateinit var colorProvider: NaviColorProvider

    @Inject
    lateinit var viewModelFactory: WeeklyViewModelFactory

    @Inject
    lateinit var episodeNavigator: EpisodeNavigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = fragmentComposeView {
        ProvideWindowInsets {
            CompositionLocalProvider(
                LocalEpisodeNavigator provides episodeNavigator
            ) {
                WeeklyContainerScreen(
                    modifier = Modifier.fillMaxSize(),
                    titles = serviceApi(),
                    selectedTabIndex = todayTabPosition,
                    viewModelFactory = viewModelFactory,
                    colorProvider = colorProvider
                )
            }
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
