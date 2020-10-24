package com.pluu.webtoon.weekly.ui

import android.graphics.Bitmap
import android.graphics.Color
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
import androidx.compose.ui.graphics.ImageAsset
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.palette.graphics.Palette
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
import dagger.hilt.android.AndroidEntryPoint
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
        // TODO: 리스트 로딩
        if (toonList?.isNotEmpty() == true) {
            LazyColumnFor(
                items = toonList!!,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 3.dp)
            ) { item ->
                WeeklyItemUi(item) { info, image ->
                    if (info.isLock) {
                        selectLockItem()
                    } else {
                        if (image != null) {
                            selectSuccess(info, image)
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            ) {
                WeeklyEmptyUi()
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

    private fun selectSuccess(item: ToonInfo, image: ImageAsset) {
        fun asyncPalette(bitmap: Bitmap, block: (PalletColor) -> Unit) {
            Palette.from(bitmap).generate { p ->
                val colors = p?.let {
                    PalletColor(
                        p.getDarkVibrantColor(Color.BLACK),
                        p.getDarkMutedColor(Color.BLACK),
                        Color.WHITE
                    )
                } ?: PalletColor(Color.BLACK, Color.BLACK, Color.WHITE)
                block(colors)
            }
        }

        asyncPalette(image.asAndroidBitmap()) { colors ->
            moveEpisode(item, colors)
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Private Function
    // /////////////////////////////////////////////////////////////////////////

    private fun favoriteUpdate(info: FavoriteResult) {
        // TODO: 즐겨찾기 갱신
//        (binding.recyclerView.adapter as? WeeklyListAdapter)
//            ?.modifyInfo(info.id, info.isFavorite)
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
