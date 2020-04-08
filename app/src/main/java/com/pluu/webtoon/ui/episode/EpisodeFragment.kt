package com.pluu.webtoon.ui.episode

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.pluu.event.EventBus
import com.pluu.kotlin.toast
import com.pluu.webtoon.R
import com.pluu.webtoon.adapter.EpisodeAdapter
import com.pluu.webtoon.common.Const
import com.pluu.webtoon.databinding.FragmentEpisodeBinding
import com.pluu.webtoon.domain.moel.EpisodeInfo
import com.pluu.webtoon.domain.moel.ToonInfo
import com.pluu.webtoon.event.FirstItemSelectEvent
import com.pluu.webtoon.model.FavoriteResult
import com.pluu.webtoon.ui.detail.DetailActivity
import com.pluu.webtoon.ui.listener.EpisodeSelectListener
import com.pluu.webtoon.ui.weekly.PalletColor
import com.pluu.webtoon.utils.MoreRefreshListener
import com.pluu.webtoon.utils.lazyNone
import com.pluu.webtoon.utils.observeNonNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * 에피소드 리스트 Fragment
 * Created by pluu on 2017-05-09.
 */
class EpisodeFragment : Fragment(),
    SwipeRefreshLayout.OnRefreshListener,
    EpisodeSelectListener {

    private val REQUEST_DETAIL = 1000

    private val viewModel: EpisodeViewModel by viewModel {
        parametersOf(
            requireArguments().getParcelable<ToonInfo>(Const.EXTRA_EPISODE)
        )
    }

    private var _binding: FragmentEpisodeBinding? = null
    private val binding get() = _binding!!

    private val palletColor by lazyNone {
        requireArguments().getParcelable<PalletColor>(Const.EXTRA_PALLET)!!
    }
    private val adapter by lazyNone {
        EpisodeAdapter(this)
    }

    private val loadDlg by lazyNone {
        ProgressDialog(context).apply {
            setCancelable(false)
            setMessage(getString(R.string.msg_loading))
        }
    }

    private var isFavorite: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        _binding = FragmentEpisodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initEvent()
        loading()
    }

    private fun initView() {
        binding.swipeRefreshWidget.setColorSchemeResources(
            R.color.color1,
            R.color.color2,
            R.color.color3,
            R.color.color4
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
        lifecycleScope.launchWhenResumed {
            registerFirstSelectEvent()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_DETAIL) {
            viewModel.readUpdate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    @FlowPreview
    @ExperimentalCoroutinesApi
    private suspend fun registerFirstSelectEvent() {
        EventBus.subscribeToEvent<FirstItemSelectEvent>()
            .collect {
                firstItemSelect()
            }
    }

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
        startActivityForResult(Intent(context, DetailActivity::class.java).apply {
            putExtras(
                bundleOf(
                    Const.EXTRA_EPISODE to item,
                    Const.EXTRA_PALLET to palletColor
                )
            )
        }, REQUEST_DETAIL)
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
