package com.pluu.webtoon.episode.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.pluu.core.utils.lazyNone
import com.pluu.utils.MoreRefreshListener
import com.pluu.utils.ProgressDialog
import com.pluu.utils.getRequiredSerializableExtra
import com.pluu.utils.observeNonNull
import com.pluu.utils.result.registerStartActivityForResult
import com.pluu.utils.toast
import com.pluu.utils.viewbinding.viewBinding
import com.pluu.webtoon.Const
import com.pluu.webtoon.episode.R
import com.pluu.webtoon.episode.databinding.FragmentEpisodeBinding
import com.pluu.webtoon.episode.ui.listener.EpisodeSelectListener
import com.pluu.webtoon.model.EpisodeInfo
import com.pluu.webtoon.model.ToonInfoWithFavorite
import com.pluu.webtoon.navigator.DetailNavigator
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

    private val viewModel by activityViewModels<EpisodeViewModel>()

    private val binding by viewBinding(FragmentEpisodeBinding::bind)

    private val palletColor by lazyNone {
        requireArguments().getRequiredSerializableExtra<PalletColor>(Const.EXTRA_PALLET)
    }
    private val adapter by lazyNone {
        EpisodeAdapter(this)
    }

    private val loadDlg by lazyNone {
        ProgressDialog.create(requireContext(), R.string.msg_loading).apply {
            setCancelable(false)
        }
    }

    @Inject
    lateinit var detailNavigator: DetailNavigator

    private val openDetailLauncher = registerStartActivityForResult {
        viewModel.readUpdate()
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

    override fun onRefresh() {
        adapter.clear()
        adapter.notifyDataSetChanged()
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
        detailNavigator.openDetail(
            context = requireContext(),
            launcher = openDetailLauncher,
            item = item,
            palletColor = palletColor
        )
    }

    companion object {
        fun newInstance(
            info: ToonInfoWithFavorite,
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
