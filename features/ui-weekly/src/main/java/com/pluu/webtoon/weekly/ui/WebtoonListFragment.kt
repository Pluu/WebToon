package com.pluu.webtoon.weekly.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.pluu.utils.observeNonNull
import com.pluu.utils.result.registerStartActivityForResult
import com.pluu.utils.result.setFragmentResult
import com.pluu.utils.toast
import com.pluu.webtoon.Const
import com.pluu.webtoon.Const.resultEpisodeLoaded
import com.pluu.webtoon.Const.resultEpisodeStart
import com.pluu.webtoon.model.ToonInfo
import com.pluu.webtoon.navigator.EpisodeNavigator
import com.pluu.webtoon.ui.compose.FragmentComposeView
import com.pluu.webtoon.ui.model.FavoriteResult
import com.pluu.webtoon.ui.model.PalletColor
import com.pluu.webtoon.weekly.R
import com.pluu.webtoon.weekly.ui.image.CoilPalletDarkCalculator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Main EpisodeResult List Fragment
 * Created by pluu on 2017-05-07.
 */
@AndroidEntryPoint
class WebtoonListFragment : Fragment() {

    private val viewModel by viewModels<WeekyViewModel>()

    private val toonViewModel by activityViewModels<ToonViewModel>()

    private val openEpisodeLauncher = registerStartActivityForResult { activityResult ->
        val favorite: FavoriteResult = activityResult.data
            ?.getParcelableExtra(Const.EXTRA_FAVORITE_EPISODE)
            ?: return@registerStartActivityForResult
        toonViewModel.updateFavorite(favorite)
    }

    @Inject
    lateinit var episodeNavigator: EpisodeNavigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentComposeView {
        val toonList by viewModel.listEvent.observeAsState()
        val palletCalculator = CoilPalletDarkCalculator(ContextAmbient.current)

        when {
            toonList == null -> {
                // 초기 Loading
                Box(
                    modifier = Modifier.fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                ) {
                    WeeklyLoadingUi()
                }
            }
            toonList!!.isNotEmpty() -> {
                // 해당 요일에 웹툰이 있을 경
                LazyColumnFor(
                    items = toonList!!,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 3.dp)
                ) { item ->
                    WeeklyItemUi(item) { info ->
                        if (info.isLock) {
                            selectLockItem()
                        } else {
                            selectSuccess(palletCalculator, info)
                        }
                    }
                }
            }
            else -> {
                // 해당 요일에 웹툰이 없을 경우
                Box(
                    modifier = Modifier.fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                ) {
                    WeeklyEmptyUi()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.event.observeNonNull(viewLifecycleOwner) { event ->
            when (event) {
                WeeklyEvent.START -> {
                    setFragmentResult(resultEpisodeStart)
                }
                WeeklyEvent.LOADED -> {
                    setFragmentResult(resultEpisodeLoaded)
                }
                is WeeklyEvent.ERROR -> {
                    toast(event.message)
                }
            }
        }
        toonViewModel.updateEvent.observeNonNull(viewLifecycleOwner) {
            favoriteUpdate(it)
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // WebToonSelectListener
    // /////////////////////////////////////////////////////////////////////////

    private fun selectLockItem() {
        toast(R.string.msg_not_support)
    }

    // /////////////////////////////////////////////////////////////////////////
    // Private Function
    // /////////////////////////////////////////////////////////////////////////

    private fun favoriteUpdate(info: FavoriteResult) {
        // TODO: 즐겨찾기 갱신
//        (binding.recyclerView.adapter as? WeeklyListAdapter)
//            ?.modifyInfo(info.id, info.isFavorite)
    }

    private fun selectSuccess(palletCalculator: CoilPalletDarkCalculator, item: ToonInfo) {
        lifecycleScope.launch {
            val palletColor = palletCalculator.calculateSwatchesInImage(item.image)
            moveEpisode(item, palletColor)
        }
    }

    private fun moveEpisode(item: ToonInfo, palletColor: PalletColor) {
        episodeNavigator.openEpisode(
            context = requireContext(),
            launcher = openEpisodeLauncher,
            item = item,
            palletColor = palletColor
        )
    }

    companion object {
        private const val EXTRA_POS = "EXTRA_POS"

        fun newInstance(position: Int): WebtoonListFragment {
            return WebtoonListFragment().apply {
                arguments = bundleOf(EXTRA_POS to position)
            }
        }
    }
}
