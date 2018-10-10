package com.pluu.webtoon.ui.episode

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.pluu.event.EventBus
import com.pluu.kotlin.toast
import com.pluu.webtoon.R
import com.pluu.webtoon.adapter.EpisodeAdapter
import com.pluu.webtoon.common.Const
import com.pluu.webtoon.event.FirstItemSelectEvent
import com.pluu.webtoon.item.EpisodeInfo
import com.pluu.webtoon.item.ToonInfo
import com.pluu.webtoon.model.FavoriteResult
import com.pluu.webtoon.ui.detail.DetailActivity
import com.pluu.webtoon.ui.listener.EpisodeSelectListener
import com.pluu.webtoon.utils.MoreRefreshListener
import com.pluu.webtoon.utils.launchWithUI
import com.pluu.webtoon.utils.lazyNone
import com.pluu.webtoon.utils.observeNonNull
import kotlinx.android.synthetic.main.fragment_episode.*
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.channels.consumeEach
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * 에피소드 리스트 Fragment
 * Created by pluu on 2017-05-09.
 */
class EpisodeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, EpisodeSelectListener {

    private val REQUEST_DETAIL = 1000

    private val viewModel: EpisodeViewModel by viewModel {
        parametersOf(
            arguments!!.getParcelable(Const.EXTRA_EPISODE) as ToonInfo
        )
    }
    private val color: IntArray by lazyNone {
        arguments!!.getIntArray(Const.EXTRA_MAIN_COLOR)
    }
    private val adapter: EpisodeAdapter by lazyNone {
        EpisodeAdapter(this)
    }

    private val loadDlg: ProgressDialog by lazyNone {
        ProgressDialog(context).apply {
            setCancelable(false)
            setMessage(getString(R.string.msg_loading))
        }
    }

    private val jobs = arrayListOf<Job>()

    private var isFavorite: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_episode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        loading()
    }

    private fun initView() {
        swipeRefreshWidget.setColorSchemeResources(
            R.color.color1,
            R.color.color2,
            R.color.color3,
            R.color.color4
        )
        swipeRefreshWidget.setOnRefreshListener(this)

        recyclerView.apply {
            layoutManager = GridLayoutManager(
                context,
                resources.getInteger(R.integer.episode_column_count)
            )
            adapter = this@EpisodeFragment.adapter
            addOnScrollListener(scrollListener)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.listEvent.observeNonNull(this) { list ->
            adapter.addItems(list)
        }
        viewModel.updateListEvent.observeNonNull(this) { list ->
            adapter.updateRead(list)
        }
        viewModel.favorite.observeNonNull(this) { isFavorite ->
            this.isFavorite = isFavorite
        }
        viewModel.event.observeNonNull(this) { event ->
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
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_episode, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        menu ?: return
        menu.findItem(R.id.menu_item_favorite_add).isVisible = !isFavorite
        menu.findItem(R.id.menu_item_favorite_delete).isVisible = isFavorite
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            activity?.finish()
            return true
        }

        when (item?.itemId) {
            // 즐겨찾기 추가
            R.id.menu_item_favorite_add -> viewModel.favorite(true)
            // 즐겨찾기 삭제
            R.id.menu_item_favorite_delete -> viewModel.favorite(false)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)

        if (newConfig?.orientation == Configuration.ORIENTATION_LANDSCAPE ||
            newConfig?.orientation == Configuration.ORIENTATION_PORTRAIT
        ) {
            adapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        jobs += launchWithUI {
            EventBus.subscribeToEvent<FirstItemSelectEvent>()
                .consumeEach {
                    firstItemSelect()
                }
        }
    }

    override fun onPause() {
        super.onPause()
        jobs.forEach { it.cancel() }
    }

    override fun onRefresh() {
        adapter.clear()
        viewModel.initalize()
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

    ///////////////////////////////////////////////////////////////////////////
    // EpisodeSelectListener
    ///////////////////////////////////////////////////////////////////////////

    override fun selectLockItem() {
        toast(R.string.msg_not_support)
    }

    override fun selectSuccess(item: EpisodeInfo) {
        moveDetailPage(item)
    }

    ///////////////////////////////////////////////////////////////////////////
    // View Function
    ///////////////////////////////////////////////////////////////////////////

    private fun closeRefreshing() {
        swipeRefreshWidget.isRefreshing = false
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
            putExtra(Const.EXTRA_EPISODE, item)
            putExtra(Const.EXTRA_MAIN_COLOR, color[0])
            putExtra(Const.EXTRA_STATUS_COLOR, color[1])
        }, REQUEST_DETAIL)
    }

    companion object {
        fun newInstance(
            info: ToonInfo,
            color: IntArray
        ): EpisodeFragment {
            return EpisodeFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(Const.EXTRA_EPISODE, info)
                    putIntArray(Const.EXTRA_MAIN_COLOR, color)
                }
            }
        }
    }
}
