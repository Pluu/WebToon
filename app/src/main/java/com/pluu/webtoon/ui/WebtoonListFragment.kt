package com.pluu.webtoon.ui

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.pluu.event.RxBusProvider
import com.pluu.kotlin.getCompatColor
import com.pluu.kotlin.toVisibleOrGone
import com.pluu.kotlin.toast
import com.pluu.support.impl.AbstractWeekApi
import com.pluu.support.impl.ServiceConst
import com.pluu.webtoon.R
import com.pluu.webtoon.adapter.MainListAdapter
import com.pluu.webtoon.common.Const
import com.pluu.webtoon.db.RealmHelper
import com.pluu.webtoon.di.Properties
import com.pluu.webtoon.event.MainEpisodeLoadedEvent
import com.pluu.webtoon.event.MainEpisodeStartEvent
import com.pluu.webtoon.item.WebToonInfo
import com.pluu.webtoon.ui.listener.WebToonSelectListener
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_webtoon_list.*
import org.koin.android.ext.android.inject
import org.koin.android.ext.android.setProperty

/**
 * Main EpisodePage List Fragment
 * Created by pluu on 2017-05-07.
 */
class WebtoonListFragment : Fragment(), WebToonSelectListener {

    private val realmHelper: RealmHelper by inject()

    private val serviceApi: AbstractWeekApi by inject(Properties.WEEK_KEY)

    private var position: Int = 0
    private val REQUEST_DETAIL = 1000

    private val disposables: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    private val toonLayoutManager: GridLayoutManager by lazy {
        GridLayoutManager(activity, resources.getInteger(R.integer.webtoon_column_count))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_webtoon_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setProperty(Properties.API_TYPE_KEY, ServiceConst.getApiType(arguments))
        position = arguments?.getInt(Const.EXTRA_POS) ?: 0

        with(recyclerView) {
            layoutManager = toonLayoutManager
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Single.fromCallable { serviceApi.parseMain(position) }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .map(favoriteProcessFunc)
            .map { unsortedList ->
                // 정렬
                unsortedList.sorted()
            }
            .doOnSubscribe { RxBusProvider.getInstance().send(MainEpisodeStartEvent()) }
            .doOnSuccess { RxBusProvider.getInstance().send(MainEpisodeLoadedEvent()) }
            .subscribe(requestSubscriber, Consumer { t ->
                RxBusProvider.getInstance().send(MainEpisodeLoadedEvent())
                Toast.makeText(activity, t.message, Toast.LENGTH_SHORT).show()
            }).let {
                disposables.add(it)
            }
    }

    override fun onDestroyView() {
        disposables.clear()
        super.onDestroyView()
    }

    private val requestSubscriber = Consumer<List<WebToonInfo>> { list ->
        val activity = activity?.takeUnless { it.isFinishing } ?: return@Consumer
        recyclerView.adapter = MainListAdapter(activity, list, this)
        emptyView.visibility = list.isEmpty().toVisibleOrGone()
    }

    private val favoriteProcessFunc = Function<List<WebToonInfo>, List<WebToonInfo>> { list ->
        list.forEach {
            it.isFavorite = realmHelper.getFavoriteToon(serviceApi.naviItem, it.toonId)
        }
        list
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_DETAIL) {
            // 즐겨찾기 변경 처리 > 다른 ViewPager의 Fragment도 수신받기위해 Referrer
            fragmentManager?.findFragmentByTag(Const.MAIN_FRAG_TAG)
                ?.onActivityResult(REQUEST_DETAIL_REFERRER, resultCode, data)
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
        view.palletBitmap?.let {
            asyncPalette(item, it)
        }
    }

    private fun asyncPalette(item: WebToonInfo, bitmap: Bitmap) {
        val context = context ?: return
        Palette.from(bitmap).generate { p ->
            val bgColor = p?.getDarkVibrantColor(Color.BLACK) ?: Color.BLACK
            val statusColor =
                p?.getDarkMutedColor(context.getCompatColor(R.color.theme_primary_dark))
                    ?: context.getCompatColor(R.color.theme_primary_dark)
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
        toonLayoutManager.spanCount = resources.getInteger(R.integer.webtoon_column_count)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)

        newConfig?.takeIf {
            it.orientation == Configuration.ORIENTATION_LANDSCAPE
                    || it.orientation == Configuration.ORIENTATION_PORTRAIT
        }.run {
            updateSpanCount()
        }
    }

    override fun selectLockItem() {
        toast(R.string.msg_not_support)
    }

    override fun selectSuccess(view: ImageView, item: WebToonInfo) {
        loadPalette(view, item)
    }

    companion object {
        const val REQUEST_DETAIL_REFERRER = 1001
    }
}

private val ImageView.palletBitmap: Bitmap?
    get() {
        val innerDrawable: Drawable = drawable
        return when (innerDrawable) {
            is BitmapDrawable -> innerDrawable.bitmap
            is GifDrawable -> innerDrawable.firstFrame
            else -> null
        }
    }