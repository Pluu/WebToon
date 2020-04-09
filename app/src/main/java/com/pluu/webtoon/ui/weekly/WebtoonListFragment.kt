package com.pluu.webtoon.ui.weekly

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.pluu.event.EventBus
import com.pluu.kotlin.toast
import com.pluu.webtoon.R
import com.pluu.webtoon.adapter.MainListAdapter
import com.pluu.webtoon.common.Const
import com.pluu.webtoon.databinding.FragmentWebtoonListBinding
import com.pluu.webtoon.domain.moel.ToonInfo
import com.pluu.webtoon.event.MainEpisodeLoadedEvent
import com.pluu.webtoon.event.MainEpisodeStartEvent
import com.pluu.webtoon.model.FavoriteResult
import com.pluu.webtoon.ui.episode.EpisodesActivity
import com.pluu.webtoon.ui.listener.WebToonSelectListener
import com.pluu.webtoon.utils.observeNonNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * Main EpisodeResult List Fragment
 * Created by pluu on 2017-05-07.
 */
class WebtoonListFragment : Fragment(), WebToonSelectListener {

    private val viewModel: WeekyViewModel by viewModel {
        parametersOf(
            arguments?.getInt(EXTRA_POS) ?: 0
        )
    }

    private val toonViewModel: ToonViewModel by sharedViewModel()

    private lateinit var binding: FragmentWebtoonListBinding

    private val REQUEST_DETAIL = 1000

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWebtoonListBinding.inflate(inflater, container, false)
        return binding.root
    }

    @ExperimentalCoroutinesApi
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
                    EventBus.send(MainEpisodeStartEvent())
                }
                WeeklyEvent.LOADED -> {
                    EventBus.send(MainEpisodeLoadedEvent())
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_DETAIL) {
            val favorite =
                data?.getParcelableExtra<FavoriteResult>(Const.EXTRA_FAVORITE_EPISODE) ?: return
            toonViewModel.updateFavorite(favorite)
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
            val context = context ?: return
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
        startActivityForResult(Intent(activity, EpisodesActivity::class.java).apply {
            putExtras(
                bundleOf(
                    Const.EXTRA_EPISODE to item,
                    Const.EXTRA_PALLET to palletColor
                )
            )
        }, REQUEST_DETAIL)
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
