package com.pluu.webtoon.ui

import android.animation.*
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.animation.DecelerateInterpolator
import com.pluu.support.impl.AbstractDetailApi
import com.pluu.support.impl.NAV_ITEM
import com.pluu.webtoon.AppController
import com.pluu.webtoon.R
import com.pluu.webtoon.common.Const
import com.pluu.webtoon.db.RealmHelper
import com.pluu.webtoon.item.*
import com.pluu.webtoon.ui.detail.*
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * 상세화면 Activity
 * Created by pluu on 2017-05-09.
 */
class DetailActivity : AppCompatActivity(), ToggleListener, FirstBindListener {

    private val TAG = DetailActivity::class.java.simpleName

    @Inject
    lateinit var realmHelper: RealmHelper

    private lateinit var serviceApi: AbstractDetailApi
    private lateinit var service: NAV_ITEM
    private lateinit var episode: Episode

    private var customTitleColor: Int = 0
    private var customStatusColor: Int = 0

    private var SWIPE_MIN_DISTANCE: Int = 0
    private var SWIPE_THRESHOLD_VELOCITY: Int = 0
    private var statusBarAnimator: ObjectAnimator? = null

    private var currentItem: Detail? = null

    private val DELAY_TIME = TimeUnit.MILLISECONDS.convert(3, TimeUnit.SECONDS)
    private var loadingFlag: Boolean = false

    private val dlg: ProgressDialog by lazy {
        ProgressDialog(this).apply {
            setMessage(getString(R.string.msg_loading))
        }
    }

    private val gd: GestureDetector by lazy {
        GestureDetector(this, listener)
    }

    private var isFragmentAttach = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        (applicationContext as AppController).realmHelperComponent.inject(this)

        setSupportActionBar(toolbar_actionbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getApi()
        initView()

        resources.displayMetrics.apply {
            SWIPE_MIN_DISTANCE = widthPixels / 3
            SWIPE_THRESHOLD_VELOCITY = widthPixels / 2
        }
    }

    override fun onResume() {
        super.onResume()
        if (!loadingFlag) {
            loading(episode)
        }
    }

    override fun onPause() {
        super.onPause()
        loadingFlag = true
    }

    private fun getApi() {
        service = intent.getSerializableExtra(Const.EXTRA_API) as NAV_ITEM
        serviceApi = AbstractDetailApi.getApi(this, service)
    }

    private fun initView() {
        episode = intent.getParcelableExtra<Episode>(Const.EXTRA_EPISODE).apply {
            tvSubTitle.text = title
        }

        customTitleColor = intent.getIntExtra(Const.EXTRA_MAIN_COLOR, Color.BLACK)
        customStatusColor = intent.getIntExtra(Const.EXTRA_STATUS_COLOR, Color.BLACK)

        btnPrev.isEnabled = false
        btnNext.isEnabled = false

        AnimatorSet().apply {
            playTogether(bgColorAnimator(), getStatusBarAnimator())
            duration = 1000L
            interpolator = DecelerateInterpolator()
            start()
        }

        arrayOf(btnPrev, btnNext).forEach {
            it.setOnClickListener(View.OnClickListener { v ->
                val link = if (v.id == R.id.btnPrev) currentItem?.prevLink else currentItem?.nextLink

                if (TextUtils.isEmpty(link)) {
                    return@OnClickListener
                }

                episode.episodeId = link!!
                loadingFlag = false
                loading(episode)
            })
        }
    }

    private fun bgColorAnimator(): Animator {
        val value = TypedValue()
        theme.resolveAttribute(R.attr.colorPrimary, value, true)

        return ValueAnimator.ofObject(ArgbEvaluator(), value.data, customTitleColor).apply {
            addUpdateListener { animation ->
                val value1 = animation.animatedValue as Int
                toolbar_actionbar.setBackgroundColor(value1)
                btnPrev.setBackgroundColor(value1)
                btnNext.setBackgroundColor(value1)
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    ViewCompat.setBackground(btnNext, stateListBgDrawable)
                    ViewCompat.setBackground(btnPrev, stateListBgDrawable)

                    btnNext.setTextColor(stateListTextDrawable)
                    btnPrev.setTextColor(stateListTextDrawable)
                }
            })
        }
    }

    private fun getStatusBarAnimator(): Animator {
        statusBarAnimator?.cancel()
        val resValue = TypedValue()
        theme.resolveAttribute(R.attr.colorPrimaryDark, resValue, true)
        statusBarAnimator = ObjectAnimator.ofInt(window, "statusBarColor", resValue.data, customTitleColor).apply {
            setEvaluator(ArgbEvaluator())
        }
        return statusBarAnimator!!
    }

    private val stateListBgDrawable: StateListDrawable
        get() {
            return StateListDrawable().apply {
                addState(intArrayOf(-android.R.attr.state_enabled), ColorDrawable(Color.GRAY))
                addState(intArrayOf(android.R.attr.state_pressed), ColorDrawable(Color.WHITE))
                addState(intArrayOf(android.R.attr.state_enabled), ColorDrawable(customTitleColor))
            }
        }

    private val stateListTextDrawable: ColorStateList
        get() {
            val state = arrayOf(intArrayOf(-android.R.attr.state_enabled), intArrayOf(android.R.attr.state_pressed), intArrayOf(android.R.attr.state_enabled))
            val colors = intArrayOf(Color.WHITE, customTitleColor, Color.WHITE)
            return ColorStateList(state, colors)
        }

    private fun loading(item: Episode) {
        Log.i(TAG, "Load Detail: " + item.toonId + ", " + item.episodeId)
        currentItem?.apply {
            prevLink = null
            nextLink = null
        }

        getRequestApi(item)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { dlg.show() }
                .doOnSuccess { dlg.dismiss() }
                .subscribe(requestSubscriber)
    }

    //	@RxLogObservable
    private fun getRequestApi(item: Episode): Single<Detail> {
        return Single.defer { Single.just(serviceApi.parseDetail(item)) }
    }

    //	@RxLogSubscriber
    private val requestSubscriber = Consumer<Detail> { item ->
        if (item?.list?.isNotEmpty() ?: false) {
            readAsync(item)

            currentItem = item
            tvTitle.text = item.title
            btnPrev.isEnabled = !TextUtils.isEmpty(item.prevLink)
            btnNext.isEnabled = !TextUtils.isEmpty(item.nextLink)

            fragmentInit(item.type)
            fragmentAttach(item.list!!)
        } else {
            val msg = (item.errorType ?: ERROR_TYPE.DEFAULT_ERROR).getMessage(baseContext)

            AlertDialog.Builder(this@DetailActivity)
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok) { _, _ -> finish() }
                    .show()
            return@Consumer
        }
    }

    private fun fragmentInit(type: DETAIL_TYPE) {
        if (isFragmentAttach) {
            return
        }

        val f: Fragment = when (type) {
            DETAIL_TYPE.DEFAULT -> DefaultDetailFragment(gd, bottomMenu.height)
            DETAIL_TYPE.DAUM_CHATTING -> DaumChattingFragment(gd)
            DETAIL_TYPE.DAUM_MULTI -> DaumMultiFragment(gd)
        }

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, f, Const.DETAIL_FRAG_TAG)
                .commit()

        isFragmentAttach = true
    }

    private fun fragmentAttach(list: List<DetailView>) {
        supportFragmentManager.findFragmentByTag(Const.DETAIL_FRAG_TAG)?.let {
            (it as BaseDetailFragment).loadView(list)
        }
    }

    /**
     * Read Detail Item
     * @param item Item
     */
    private fun readAsync(item: Detail) {
        realmHelper.readEpisode(service, item)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }

        when (item.itemId) {
            R.id.menu_item_share ->
                // 공유하기
                currentItem?.let {
                    startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                        val sender = serviceApi.getDetailShare(episode, it)
                        Log.i(TAG, "Share=" + sender)
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, sender.title)
                        putExtra(Intent.EXTRA_TEXT, sender.url)
                    }, "Share"))
                }
        }

        return super.onOptionsItemSelected(item)
    }

    private val mToggleHandler = Handler {
        toggleHideBar()
        true
    }

    /**
     * Detects and toggles immersive mode.
     */
    private fun toggleHideBar() {
        val uiOptions = window.decorView.systemUiVisibility

        if (uiOptions and View.SYSTEM_UI_FLAG_LOW_PROFILE == 0) {
            moveToAxisY(toolbar_actionbar, true)
            moveToAxisY(bottomMenu, false)
        } else {
            moveRevert(toolbar_actionbar)
            moveRevert(bottomMenu)
        }

        var newUiOptions = uiOptions
        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_LOW_PROFILE
        window.decorView.systemUiVisibility = newUiOptions
    }

    private fun moveToAxisY(view: View, isToTop: Boolean) {
        view.animate()
                .translationY((if (isToTop) -view.height else view.height).toFloat())
                .start()
    }

    private fun moveRevert(view: View) {
        view.animate().translationY(0f).start()
    }

    private val listener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            toggleDelay(false)
            return true
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            if (e1.x - e2.x > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                btnNext.performClick()
                return true
            } else if (e2.x - e1.x > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                btnPrev.performClick()
                return true
            }

            return super.onFling(e1, e2, velocityX, velocityY)
        }
    }

    private fun toggleDelay(isDelay: Boolean) {
        val TOGGLE_ID = 0
        mToggleHandler.removeMessages(TOGGLE_ID)
        mToggleHandler.sendEmptyMessageDelayed(TOGGLE_ID, if (isDelay) DELAY_TIME else 0)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun finish() {
        setResult(Activity.RESULT_OK)
        super.finish()
    }

    override fun childCallToggle(isDelay: Boolean) {
        toggleDelay(isDelay)
    }

    override fun loadingHide() {
        dlg.dismiss()
    }

    override fun firstBind() {
        if (currentItem?.list?.isNotEmpty() ?: false) {
            fragmentAttach(currentItem!!.list!!)
        }
    }

}
