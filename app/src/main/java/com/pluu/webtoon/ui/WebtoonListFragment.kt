package com.pluu.webtoon.ui

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.pluu.event.RxBusProvider
import com.pluu.support.impl.AbstractWeekApi
import com.pluu.support.impl.ServiceConst
import com.pluu.webtoon.AppController
import com.pluu.webtoon.R
import com.pluu.webtoon.adapter.MainListAdapter
import com.pluu.webtoon.common.Const
import com.pluu.webtoon.db.RealmHelper
import com.pluu.webtoon.event.MainEpisodeLoadedEvent
import com.pluu.webtoon.event.MainEpisodeStartEvent
import com.pluu.webtoon.item.WebToonInfo
import com.pluu.webtoon.ui.listener.WebToonSelectListener
import com.pluu.webtoon.utils.glideBitmap
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_webtoon_list.*
import java.util.*
import javax.inject.Inject

/**
 * Main EpisodePage List Fragment
 * Created by pluu on 2017-05-07.
 */
class WebtoonListFragment : Fragment(), WebToonSelectListener {

    @Inject
    lateinit var realmHelper: RealmHelper

    private lateinit var serviceApi: AbstractWeekApi

    private var position: Int = 0
    private val REQUEST_DETAIL = 1000

    private val manager: GridLayoutManager by lazy {
        GridLayoutManager(activity, resources.getInteger(R.integer.webtoon_column_count))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (context.applicationContext as AppController).realmHelperComponent.inject(this)

        serviceApi = AbstractWeekApi.getApi(context, ServiceConst.getApiType(arguments))
        position = arguments.getInt(Const.EXTRA_POS)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_webtoon_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.apply {
            layoutManager = manager
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        apiRequest
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(favoriteProcessFunc)
                .map { unsortedList ->
                    // 정렬
                    Collections.sort(unsortedList)
                    unsortedList
                }
                .doOnSubscribe { RxBusProvider.getInstance().send(MainEpisodeStartEvent()) }
                .doOnSuccess { RxBusProvider.getInstance().send(MainEpisodeLoadedEvent()) }
                .subscribe(requestSubscriber, Consumer { t ->
                    RxBusProvider.getInstance().send(MainEpisodeLoadedEvent())
                    Toast.makeText(activity, t.message, Toast.LENGTH_SHORT).show()
                })
    }

    //	@RxLogSubscriber
    private val requestSubscriber = Consumer<List<WebToonInfo>> { list ->
        val activity = activity
        if (activity == null || activity.isFinishing) {
            return@Consumer
        }
        recyclerView.adapter = MainListAdapter(activity, list, this)
    }

    private val favoriteProcessFunc = Function<List<WebToonInfo>, List<WebToonInfo>> { list ->
        for (item in list) {
            item.isFavorite = realmHelper.getFavoriteToon(serviceApi.naviItem, item.toonId)
        }
        list
    }

    //	@RxLogObservable
    private val apiRequest: Single<List<WebToonInfo>>
        get() = Single.fromCallable { serviceApi.parseMain(position) }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_DETAIL) {
            // 즐겨찾기 변경 처리 > 다른 ViewPager의 Fragment도 수신받기위해 Referrer
            fragmentManager.findFragmentByTag(Const.MAIN_FRAG_TAG)?.
                    onActivityResult(REQUEST_DETAIL_REFERRER, resultCode, data)
        } else if (requestCode == REQUEST_DETAIL_REFERRER) {
            // ViewPager 로부터 전달받은 Referrer
            data?.getParcelableExtra<WebToonInfo>(Const.EXTRA_EPISODE)?.apply {
                favoriteUpdate(this)
            }
        }
    }

    private fun favoriteUpdate(info: WebToonInfo) {
        val adapter = recyclerView.adapter as MainListAdapter
        val position = adapter.modifyInfo(info)
        if (position != -1) {
            adapter.notifyItemChanged(position)
        }
    }

    private fun loadPalette(view: ImageView, item: WebToonInfo) {
        glideBitmap(view)?.let {
            asyncPalette(item, it)
        }
    }

    private fun asyncPalette(item: WebToonInfo, bitmap: Bitmap) {
        val context = activity
        Palette.from(bitmap).generate { p ->
            val bgColor = p.getDarkVibrantColor(
                    Color.BLACK)
            val statusColor = p.getDarkMutedColor(
                    ContextCompat.getColor(context, R.color.theme_primary_dark))
            moveEpisode(item, bgColor, statusColor)
        }
    }

    private fun moveEpisode(item: WebToonInfo, bgColor: Int, statusColor: Int) {
        startActivityForResult(Intent(activity, EpisodesActivity::class.java).apply {
            putExtra(Const.EXTRA_API, serviceApi.naviItem)
            putExtra(Const.EXTRA_EPISODE, item)
            putExtra(Const.EXTRA_MAIN_COLOR, bgColor)
            putExtra(Const.EXTRA_STATUS_COLOR, statusColor)
        }, REQUEST_DETAIL)
    }

    private fun updateSpanCount() {
        manager.spanCount = resources.getInteger(R.integer.webtoon_column_count)
        recyclerView.adapter.notifyDataSetChanged()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)

        if (newConfig?.orientation == Configuration.ORIENTATION_LANDSCAPE
                || newConfig?.orientation == Configuration.ORIENTATION_PORTRAIT) {
            updateSpanCount()
        }
    }

    override fun selectLockItem() {
        Toast.makeText(context, R.string.msg_not_support, Toast.LENGTH_SHORT).show()
    }

    override fun selectSuccess(view: ImageView, item: WebToonInfo) {
        loadPalette(view, item)
    }

    companion object {
        val REQUEST_DETAIL_REFERRER = 1001
    }
}