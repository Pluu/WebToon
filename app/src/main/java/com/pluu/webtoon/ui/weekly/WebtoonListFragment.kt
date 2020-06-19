package com.pluu.webtoon.ui.weekly

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.pluu.kotlin.toast
import com.pluu.webtoon.R
import com.pluu.webtoon.common.Const
import com.pluu.webtoon.databinding.FragmentWebtoonListBinding
import com.pluu.webtoon.domain.moel.ToonInfo
import com.pluu.webtoon.model.FavoriteResult
import com.pluu.webtoon.ui.episode.EpisodesActivity
import com.pluu.webtoon.ui.listener.WebToonSelectListener
import com.pluu.webtoon.utils.observeNonNull
import com.pluu.webtoon.utils.result.justSafeRegisterForActivityResult
import com.pluu.webtoon.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main EpisodeResult List Fragment
 * Created by pluu on 2017-05-07.
 */
@AndroidEntryPoint
class WebtoonListFragment : Fragment(
    R.layout.fragment_webtoon_list
), WebToonSelectListener {

    private val viewModel by viewModels<WeekyViewModel>()

    private val toonViewModel by activityViewModels<ToonViewModel>()

    private val binding by viewBinding(FragmentWebtoonListBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager =
            GridLayoutManager(context, resources.getInteger(R.integer.webtoon_column_count))

        viewModel.listEvent.observeNonNull(viewLifecycleOwner) { list ->
            binding.recyclerView.adapter = MainListAdapter(requireContext(), list, this)
            binding.emptyView.isVisible = list.isEmpty()
        }
        viewModel.event.observeNonNull(viewLifecycleOwner) { event ->
            when (event) {
                WeeklyEvent.START -> {
                    setFragmentResult(resultRequestEpisodeStart, Bundle())
                }
                WeeklyEvent.LOADED -> {
                    setFragmentResult(resultRequestEpisodeLoaded, Bundle())
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

    override fun selectLockItem() {
        toast(R.string.msg_not_support)
    }

    override fun selectSuccess(view: ImageView, item: ToonInfo) {
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

        fun loadPalette(view: ImageView, block: (PalletColor) -> Unit) {
            view.palletBitmap?.let {
                asyncPalette(it, block)
            }
        }

        loadPalette(view) { colors ->
            moveEpisode(item, colors)
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Private Function
    // /////////////////////////////////////////////////////////////////////////

    private fun favoriteUpdate(info: FavoriteResult) {
        (binding.recyclerView.adapter as? MainListAdapter)
            ?.modifyInfo(info.id, info.isFavorite)
    }

    private fun moveEpisode(item: ToonInfo, palletColor: PalletColor) {
        justSafeRegisterForActivityResult(
            Intent(activity, EpisodesActivity::class.java).apply {
                putExtras(
                    bundleOf(
                        Const.EXTRA_EPISODE to item,
                        Const.EXTRA_PALLET to palletColor
                    )
                )
            }
        ) { activityResult ->
            val favorite: FavoriteResult =
                activityResult.data?.getParcelableExtra(Const.EXTRA_FAVORITE_EPISODE)
                    ?: return@justSafeRegisterForActivityResult
            toonViewModel.updateFavorite(favorite)
        }
    }

    companion object {
        private const val EXTRA_POS = "EXTRA_POS"
        const val resultRequestEpisodeStart = "resultRequestEpisodeStart"
        const val resultRequestEpisodeLoaded = "resultRequestEpisodeLoaded"

        fun newInstance(position: Int): WebtoonListFragment {
            val fragment = WebtoonListFragment()
            fragment.arguments = Bundle().apply {
                putInt(EXTRA_POS, position)
            }
            return fragment
        }
    }
}

private val ImageView.palletBitmap: Bitmap?
    get() {
        return when (val innerDrawable = drawable) {
            is BitmapDrawable -> innerDrawable.bitmap
            is GifDrawable -> innerDrawable.firstFrame
            else -> null
        }
    }
