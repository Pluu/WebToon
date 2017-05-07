package com.pluu.webtoon.ui

import android.animation.AnimatorSet
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.pluu.event.RxBusProvider
import com.pluu.support.impl.AbstractWeekApi
import com.pluu.support.impl.NAV_ITEM
import com.pluu.support.impl.ServiceConst
import com.pluu.webtoon.R
import com.pluu.webtoon.adapter.MainFragmentAdapter
import com.pluu.webtoon.common.Const
import com.pluu.webtoon.event.MainEpisodeLoadedEvent
import com.pluu.webtoon.event.MainEpisodeStartEvent
import com.pluu.webtoon.event.ThemeEvent
import com.pluu.webtoon.utils.animatorStatusBarColor
import com.pluu.webtoon.utils.animatorToolbarColor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_toon.*

/**
 * Main View Fragment
 * Created by pluu on 2017-05-07.
 */
class MainFragment : Fragment() {

    private val TAG = MainFragment::class.java.simpleName

    private var mCompositeDisposable = CompositeDisposable()

    private var isFirstDlg = true

    private val loadDlg: ProgressDialog by lazy {
        ProgressDialog(activity).apply {
            setCancelable(false)
            setMessage(getString(R.string.msg_loading))
        }
    }

    private lateinit var service: NAV_ITEM
    private lateinit var serviceApi: AbstractWeekApi
    private var listener: BindServiceListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        service = ServiceConst.getApiType(arguments).apply {
            listener?.bindNavItem(this)
        }
        serviceApi = AbstractWeekApi.getApi(context, service)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_toon, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getApi()

        viewPager.apply {
            adapter = MainFragmentAdapter(fragmentManager, serviceApi)
            // 금일 기준으로 ViewPager 기본 표시
            currentItem = serviceApi.todayTabPosition
        }

        slidingTabLayout.setCustomTabView(R.layout.view_sliding_tab,
                android.R.id.text1)
        slidingTabLayout.setViewPager(viewPager)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as BindServiceListener
    }

    private fun getApi() {
        // 선택한 서비스에 맞는 컬러 테마 변경
        setServiceTheme(serviceApi)
    }

    private fun setServiceTheme(serviceApi: AbstractWeekApi) {
        val color = serviceApi.getTitleColor(context)
        val colorDark = serviceApi.getTitleColorDark(context)
        val activity = activity

        if (activity is AppCompatActivity) {
            val animator1 = activity.animatorToolbarColor(color)
            val animator2 = activity.animatorStatusBarColor(colorDark)

            val set = AnimatorSet()
            set.playTogether(animator1, animator2)
            set.duration = 250L
            set.start()
        }

        RxBusProvider.getInstance().send(ThemeEvent(color, colorDark))
        slidingTabLayout?.setSelectedIndicatorColors(color)
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
        Glide.with(this).pauseRequests()
        mCompositeDisposable.clear()
        super.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == WebtoonListFragment.REQUEST_DETAIL_REFERRER) {
            // 포함되어있는 ViewPager 의 Fragment 갱신 처리
            (viewPager.adapter as MainFragmentAdapter).onActivityResult(requestCode, resultCode, data!!)
        }
    }

    private val busEvent = Consumer<Any> {
        when (it) {
            is MainEpisodeStartEvent -> eventStartEvent()
            is MainEpisodeLoadedEvent -> eventLoadedEvent()
        }
    }

    private fun eventStartEvent() {
        if (isFirstDlg) {
            Log.d(TAG, "eventStartEvent")
            loadDlg.show()
            isFirstDlg = false
        }
    }

    private fun eventLoadedEvent() {
        if (!isFirstDlg) {
            Log.d(TAG, "eventLoadedEvent")
            loadDlg.dismiss()
        }
    }

    companion object {

        fun newInstance(item: NAV_ITEM) =
                MainFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(Const.EXTRA_API, item)
                    }
                }
    }

    interface BindServiceListener {
        fun bindNavItem(item: NAV_ITEM)
    }

}
