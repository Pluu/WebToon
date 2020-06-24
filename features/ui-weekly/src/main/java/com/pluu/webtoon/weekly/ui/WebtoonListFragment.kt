package com.pluu.webtoon.weekly.ui

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.pluu.utils.observeNonNull
import com.pluu.utils.toast
import com.pluu.utils.viewbinding.viewBinding
import com.pluu.webtoon.AppNavigator
import com.pluu.webtoon.Const
import com.pluu.webtoon.Const.resultEpisodeLoaded
import com.pluu.webtoon.Const.resultEpisodeStart
import com.pluu.webtoon.domain.model.ToonInfo
import com.pluu.webtoon.ui.model.FavoriteResult
import com.pluu.webtoon.ui.model.PalletColor
import com.pluu.webtoon.weekly.R
import com.pluu.webtoon.weekly.databinding.FragmentWebtoonListBinding
import com.pluu.webtoon.weekly.ui.listener.WebToonSelectListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Main EpisodeResult List Fragment
 * Created by pluu on 2017-05-07.
 */
@AndroidEntryPoint
class WebtoonListFragment : Fragment(R.layout.fragment_webtoon_list) {

    private val viewModel by viewModels<WeekyViewModel>()

    private val toonViewModel by activityViewModels<ToonViewModel>()

    private val binding by viewBinding(FragmentWebtoonListBinding::bind)

    @Inject
    lateinit var appNavigator: AppNavigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager =
            GridLayoutManager(context, resources.getInteger(R.integer.webtoon_column_count))

        viewModel.listEvent.observeNonNull(viewLifecycleOwner) { list ->
            binding.recyclerView.adapter = WeeklyListAdapter(
                requireContext(),
                list,
                object : WebToonSelectListener {
                    override fun selectLockItem() {
                        this@WebtoonListFragment.selectLockItem()
                    }

                    override fun selectSuccess(view: ImageView, item: ToonInfo) {
                        this@WebtoonListFragment.selectSuccess(view, item)
                    }
                }
            )
            binding.emptyView.isVisible = list.isEmpty()
        }
        viewModel.event.observeNonNull(viewLifecycleOwner) { event ->
            when (event) {
                WeeklyEvent.START -> {
                    setFragmentResult(resultEpisodeStart, Bundle())
                }
                WeeklyEvent.LOADED -> {
                    setFragmentResult(resultEpisodeLoaded, Bundle())
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

    private fun selectSuccess(view: ImageView, item: ToonInfo) {
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
        (binding.recyclerView.adapter as? WeeklyListAdapter)
            ?.modifyInfo(info.id, info.isFavorite)
    }

    private fun moveEpisode(item: ToonInfo, palletColor: PalletColor) {
        appNavigator.openEpisode(
            context = requireContext(),
            caller = this,
            item = item,
            palletColor = palletColor
        ) { activityResult ->
            val favorite: FavoriteResult =
                activityResult.data?.getParcelableExtra(Const.EXTRA_FAVORITE_EPISODE)
                    ?: return@openEpisode
            toonViewModel.updateFavorite(favorite)
        }
    }

    companion object {
        private const val EXTRA_POS = "EXTRA_POS"

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
