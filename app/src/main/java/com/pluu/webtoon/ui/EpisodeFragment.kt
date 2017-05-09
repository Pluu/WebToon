package com.pluu.webtoon.ui

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.pluu.event.RxBusProvider
import com.pluu.support.impl.AbstractEpisodeApi
import com.pluu.support.impl.NAV_ITEM
import com.pluu.webtoon.AppController
import com.pluu.webtoon.R
import com.pluu.webtoon.adapter.EpisodeAdapter
import com.pluu.webtoon.common.Const
import com.pluu.webtoon.db.RealmHelper
import com.pluu.webtoon.event.FirstItemSelectEvent
import com.pluu.webtoon.item.Episode
import com.pluu.webtoon.item.WebToonInfo
import com.pluu.webtoon.model.REpisode
import com.pluu.webtoon.ui.listener.EpisodeSelectListener
import com.pluu.webtoon.utils.MoreRefreshListener
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_episode.*
import javax.inject.Inject

/**
 * 에피소드 리스트 Fragment
 * Created by pluu on 2017-05-09.
 */
class EpisodeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, EpisodeSelectListener {

    private val TAG = EpisodeFragment::class.java.simpleName
    private val REQUEST_DETAIL = 1000

    @Inject
    lateinit var realmHelper: RealmHelper

    lateinit var service: NAV_ITEM
    private lateinit var serviceApi: AbstractEpisodeApi
    lateinit var info: WebToonInfo
    private lateinit var color: IntArray

    private val manager: GridLayoutManager by lazy {
        GridLayoutManager(context, resources.getInteger(R.integer.episode_column_count))
    }

    private val adapter: EpisodeAdapter by lazy {
        EpisodeAdapter(context, this)
    }

    private val loadDlg: ProgressDialog by lazy {
        ProgressDialog(context).apply {
            setCancelable(false)
            setMessage(getString(R.string.msg_loading))
        }
    }

    private var nextLink: String? = null

    private var mCompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (context.applicationContext as AppController).realmHelperComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_episode, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments.apply {
            service = getSerializable(Const.EXTRA_API) as NAV_ITEM
            serviceApi = AbstractEpisodeApi.getApi(context, service)
            info = getParcelable<WebToonInfo>(Const.EXTRA_EPISODE)
            color = getIntArray(Const.EXTRA_MAIN_COLOR)
        }

        initView()
        loading()
    }

    private fun initView() {
        swipeRefreshWidget.setColorSchemeResources(
                R.color.color1,
                R.color.color2,
                R.color.color3,
                R.color.color4)
        swipeRefreshWidget.setOnRefreshListener(this)

        recyclerView.apply {
            layoutManager = manager
            adapter = this@EpisodeFragment.adapter
            addOnScrollListener(scrollListener)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)

        if (newConfig?.orientation == Configuration.ORIENTATION_LANDSCAPE
                || newConfig?.orientation == Configuration.ORIENTATION_PORTRAIT) {
            adapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        Glide.with(this).resumeRequests()
        mCompositeDisposable.add(
                RxBusProvider.getInstance()
                        .toObservable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(busEvent)
        )
    }

    override fun onPause() {
        super.onPause()
        Glide.with(this).pauseRequests()
        mCompositeDisposable.clear()
    }

    override fun onRefresh() {
        adapter.clear()
        serviceApi.init()
        loading()
    }

    private val scrollListener = object : MoreRefreshListener() {
        override fun onMoreRefresh() {
            moreLoad()
        }
    }

    private fun moreLoad() {
        if (nextLink?.isNotEmpty() ?: false) {
            Log.i(TAG, "Next Page Link=" + nextLink)
            loading()
            nextLink = null
        }
    }

    private fun loading() {
        if (swipeRefreshWidget.isRefreshing) {
            swipeRefreshWidget.isRefreshing = false
        }

        Single.zip(requestApi, readAction(), requestReadAction)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loadDlg.show() }
                .doOnSuccess { loadDlg.dismiss() }
                .doOnError { throwable -> Toast.makeText(activity, throwable.message, Toast.LENGTH_SHORT).show() }
                .subscribe(requestSubscriber)
    }

    //	@RxLogObservable
    private val requestApi: Single<List<Episode>>
        get() = Single.defer {
            Log.i(TAG, "Load Episode=" + info.toonId)
            val episodePage = serviceApi.parseEpisode(info)
            val list = episodePage.episodes
            nextLink = episodePage.moreLink()
            if (nextLink?.isNotEmpty() ?: false) {
                scrollListener.setLoadingMorePause()
            }
            Single.just(list)
        }

    fun readAction(): Single<List<REpisode>> =
            Single.defer { Single.just(realmHelper.getEpisode(service, info.toonId)) }

    private val requestReadAction = BiFunction<List<Episode>, List<REpisode>, List<Episode>> { list, readList ->
        for (readItem in readList) {
            for (episode in list) {
                if (TextUtils.equals(readItem.episodeId, episode.episodeId)) {
                    episode.setReadFlag()
                    break
                }
            }
        }
        list
    }

    private val requestSubscriber = Consumer<List<Episode>> { episodes ->
        if (episodes == null || episodes.isEmpty()) {
            if (episodes == null) {
                Toast.makeText(context, R.string.network_fail, Toast.LENGTH_SHORT).show()
                activity.finish()
            }
            return@Consumer
        }
        adapter.addItems(episodes)
        adapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_DETAIL) {
            readUpdate()
        }
    }

    private val busEvent = Consumer<Any> {
        when (it) {
            is FirstItemSelectEvent -> firstItemSelect()
        }
    }

    private fun readUpdate() {
        readAction()
                .flatMapObservable { episodes ->
                    Observable.fromIterable(episodes)
                            .map { it.episodeId }
                }
                .toList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loadDlg.show() }
                .doOnSuccess { loadDlg.dismiss() }
                .subscribe({
                    adapter.updateRead(it)
                    adapter.notifyDataSetChanged()
                }, { })
    }

    private fun firstItemSelect() {
        val item = adapter.getItem(0)
        if (item.isLock) {
            Toast.makeText(context, R.string.msg_not_support, Toast.LENGTH_SHORT).show()
            return
        }

        val firstItem = serviceApi.getFirstEpisode(item) ?: return
        firstItem.title = this.info.title
        moveDetailPage(firstItem)
    }

    private fun moveDetailPage(item: Episode) {
        startActivityForResult(Intent(context, DetailActivity::class.java).apply {
            putExtra(Const.EXTRA_API, service)
            putExtra(Const.EXTRA_EPISODE, item)
            putExtra(Const.EXTRA_MAIN_COLOR, color[0])
            putExtra(Const.EXTRA_STATUS_COLOR, color[1])
        }, REQUEST_DETAIL)
    }

    override fun selectLockItem() {
        Toast.makeText(context, R.string.msg_not_support, Toast.LENGTH_SHORT).show()
    }

    override fun selectSuccess(item: Episode) {
        moveDetailPage(item)
    }

    companion object {

        fun newInstance(service: NAV_ITEM,
                        info: WebToonInfo,
                        color: IntArray): EpisodeFragment {
            return EpisodeFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(Const.EXTRA_API, service)
                    putParcelable(Const.EXTRA_EPISODE, info)
                    putIntArray(Const.EXTRA_MAIN_COLOR, color)
                }
            }
        }
    }
}
