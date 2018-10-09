package com.pluu.webtoon.ui.weekly

import android.animation.AnimatorSet
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.pluu.event.RxBusProvider
import com.pluu.support.impl.NaviColorProvider
import com.pluu.webtoon.R
import com.pluu.webtoon.adapter.MainFragmentAdapter
import com.pluu.webtoon.data.impl.AbstractWeekApi
import com.pluu.webtoon.event.MainEpisodeLoadedEvent
import com.pluu.webtoon.event.MainEpisodeStartEvent
import com.pluu.webtoon.event.ThemeEvent
import com.pluu.webtoon.utils.animatorStatusBarColor
import com.pluu.webtoon.utils.animatorToolbarColor
import com.pluu.webtoon.utils.lazyNone
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_toon.*
import org.koin.android.ext.android.inject

/**
 * Main View Fragment
 * Created by pluu on 2017-05-07.
 */
class MainFragment : Fragment() {

    private val TAG = MainFragment::class.java.simpleName

    private var mCompositeDisposable = CompositeDisposable()

    private var isFirstDlg = true

    private val loadDlg: Dialog by lazyNone {
        ProgressDialog(activity).apply {
            setCancelable(false)
            setMessage(getString(R.string.msg_loading))
        }
    }

    private val serviceApi: AbstractWeekApi by inject()
    private val colorProvider: NaviColorProvider by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_toon, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setServiceTheme()

        viewPager.apply {
            adapter = fragmentManager?.let { MainFragmentAdapter(it, serviceApi) }
            // 금일 기준으로 ViewPager 기본 표시
            currentItem = serviceApi.todayTabPosition
        }

        slidingTabLayout.setupWithViewPager(viewPager)
    }

    // 선택한 서비스에 맞는 컬러 테마 변경
    private fun setServiceTheme() {
        val color = colorProvider.getTitleColor()
        val colorDark = colorProvider.getTitleColorDark()
        val activity = activity

        if (activity is AppCompatActivity) {
            val animator1 = activity.animatorToolbarColor(color)
            val animator2 = activity.animatorStatusBarColor(colorDark)

            AnimatorSet().apply {
                duration = 250L
                playTogether(animator1, animator2)
            }.start()
        }

        RxBusProvider.instance.send(ThemeEvent(color, colorDark))
        slidingTabLayout?.setSelectedTabIndicatorColor(color)
    }

    override fun onResume() {
        super.onResume()
        mCompositeDisposable.add(
            RxBusProvider.instance
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(busEvent)
        )
    }

    override fun onPause() {
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
            (viewPager.adapter as MainFragmentAdapter).onActivityResult(
                requestCode,
                resultCode,
                data!!
            )
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
        fun newInstance() = MainFragment()
    }
}
