package com.pluu.webtoon.episode.ui

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.pluu.core.utils.lazyNone
import com.pluu.utils.MoreRefreshListener
import com.pluu.utils.ProgressDialog
import com.pluu.utils.getRequiredParcelableExtra
import com.pluu.utils.observeNonNull
import com.pluu.utils.toast
import com.pluu.utils.viewbinding.viewBinding
import com.pluu.webtoon.AppNavigator
import com.pluu.webtoon.Const
import com.pluu.webtoon.domain.moel.EpisodeInfo
import com.pluu.webtoon.domain.moel.ToonInfo
import com.pluu.webtoon.episode.R
import com.pluu.webtoon.episode.databinding.FragmentEpisodeBinding
import com.pluu.webtoon.episode.ui.listener.EpisodeSelectListener
import com.pluu.webtoon.ui.model.FavoriteResult
import com.pluu.webtoon.ui.model.PalletColor
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * 에피소드 리스트 Fragment
 * Created by pluu on 2017-05-09.
 */
@AndroidEntryPoint
class EpisodeFragment : Fragment(R.layout.fragment_episode),
    SwipeRefreshLayout.OnRefreshListener,
    EpisodeSelectListener {

    private val viewModel by viewModels<EpisodeViewModel>()

    private val binding by viewBinding(FragmentEpisodeBinding::bind)

    private val palletColor by lazyNone {
        requireArguments().getRequiredParcelableExtra<PalletColor>(Const.EXTRA_PALLET)
    }
    private val adapter by lazyNone {
        EpisodeAdapter(this)
    }

    private val loadDlg by lazyNone {
        ProgressDialog.create(requireContext(), R.string.msg_loading).apply {
            setCancelable(false)
        }
    }

    private var isFavorite: Boolean = false

    @Inject
    lateinit var appNavigator: AppNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initEvent()
        loading()
    }

    private fun initView() {
        binding.swipeRefreshWidget.setColorSchemeResources(
            R.color.google_color_green,
            R.color.google_color_red,
            R.color.google_color_blue,
            R.color.google_color_yellow
        )
        binding.swipeRefreshWidget.setOnRefreshListener(this)

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(
                context,
                resources.getInteger(R.integer.episode_column_count)
            )
            adapter = this@EpisodeFragment.adapter
            addOnScrollListener(scrollListener)
        }
    }

    private fun initEvent() {
        viewModel.listEvent.observeNonNull(viewLifecycleOwner) { list ->
            adapter.addItems(list)
        }
        viewModel.updateListEvent.observeNonNull(viewLifecycleOwner) { list ->
            adapter.updateRead(list)
        }
        viewModel.favorite.observeNonNull(viewLifecycleOwner) { isFavorite ->
            this.isFavorite = isFavorite
        }
        viewModel.event.observeNonNull(viewLifecycleOwner) { event ->
            when (event) {
                EpisodeEvent.START -> {
                    scrollListener.setLoadingMorePause()
                    loadDlg.show()
                }
                EpisodeEvent.LOADED -> {
                    loadDlg.dismiss()
                    closeRefreshing()
                }
                is EpisodeEvent.UPDATE_FAVORITE -> {
                    toast(
                        if (event.isFavorite) R.string.favorite_add else R.string.favorite_delete
                    )
                    activity?.run {
                        setResult(Activity.RESULT_OK, Intent().apply {
                            putExtra(
                                Const.EXTRA_FAVORITE_EPISODE,
                                FavoriteResult(event.id, event.isFavorite)
                            )
                        })
                    }
                }
                is EpisodeEvent.FIRST -> moveDetailPage(event.firstEpisode)
                is EpisodeEvent.ERROR -> {
                    loadDlg.dismiss()
                    closeRefreshing()
                    toast(R.string.network_fail)
                    activity?.finish()
                }
            }
        }

        setFragmentResultListener(EpisodeConst.ShowFirst) { _, _ ->
            firstItemSelect()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_episode, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.menu_item_favorite_add).isVisible = !isFavorite
        menu.findItem(R.id.menu_item_favorite_delete).isVisible = isFavorite
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            activity?.finish()
            return true
        }

        when (item.itemId) {
            // 즐겨찾기 추가
            R.id.menu_item_favorite_add -> viewModel.favorite(true)
            // 즐겨찾기 삭제
            R.id.menu_item_favorite_delete -> viewModel.favorite(false)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ||
            newConfig.orientation == Configuration.ORIENTATION_PORTRAIT
        ) {
            adapter.notifyDataSetChanged()
        }
    }

    override fun onRefresh() {
        adapter.clear()
        viewModel.initialize()
        loading()
    }

    // /////////////////////////////////////////////////////////////////////////

    override fun selectLockItem() {
        toast(R.string.msg_not_support)
    }

    // /////////////////////////////////////////////////////////////////////////
    // EpisodeSelectListener

    override fun selectSuccess(item: EpisodeInfo) {
        moveDetailPage(item)
    }

    // /////////////////////////////////////////////////////////////////////////
    // View Function
    // /////////////////////////////////////////////////////////////////////////

    private fun closeRefreshing() {
        binding.swipeRefreshWidget.isRefreshing = false
    }

    private val scrollListener = object : MoreRefreshListener() {
        override fun onMoreRefresh() {
            loading()
        }
    }

    private fun loading() {
        viewModel.load()
    }

    private fun firstItemSelect() {
        val item = adapter.getItem(0)
        if (item.isLock) {
            toast(R.string.msg_not_support)
            return
        }
        viewModel.requestFirst()
    }

    private fun moveDetailPage(item: EpisodeInfo) {
        appNavigator.openDetail(
            context = requireContext(),
            caller = this,
            item = item,
            palletColor = palletColor
        ) {
            viewModel.readUpdate()
        }
    }

    companion object {
        fun newInstance(
            info: ToonInfo,
            color: PalletColor
        ): EpisodeFragment {
            return EpisodeFragment().apply {
                arguments = bundleOf(
                    Const.EXTRA_EPISODE to info,
                    Const.EXTRA_PALLET to color
                )
            }
        }
    }
}
