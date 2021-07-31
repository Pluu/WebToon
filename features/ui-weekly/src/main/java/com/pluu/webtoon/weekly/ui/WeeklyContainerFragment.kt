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
import com.pluu.webtoon.domain.usecase.WeeklyUseCase
import com.pluu.webtoon.navigator.EpisodeNavigator
import com.pluu.webtoon.ui.compose.fragmentComposeView
import com.pluu.webtoon.ui.compose.navigator.LocalEpisodeNavigator
import com.pluu.webtoon.weekly.event.ThemeEvent
import com.pluu.webtoon.weekly.provider.NaviColorProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
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

    @Inject
    lateinit var viewModelFactory: WeeklyViewModelFactory

    @Inject
    lateinit var episodeNavigator: EpisodeNavigator

    @OptIn(InternalCoroutinesApi::class)
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
                    serviceApi = serviceApi,
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
