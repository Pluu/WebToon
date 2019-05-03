package com.pluu.webtoon.ui.detail

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
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.commit
import com.pluu.webtoon.R
import com.pluu.webtoon.common.Const
import com.pluu.webtoon.item.EpisodeInfo
import com.pluu.webtoon.item.ShareItem
import com.pluu.webtoon.item.getMessage
import com.pluu.webtoon.utils.lazyNone
import com.pluu.webtoon.utils.observeNonNull
import kotlinx.android.synthetic.main.activity_detail.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.concurrent.TimeUnit

/**
 * 상세화면 Activity
 * Created by pluu on 2017-05-09.
 */
class DetailActivity : AppCompatActivity(), ToggleListener, FirstBindListener {

    private val viewModel: DetailViewModel by viewModel {
        parametersOf(
            intent.getParcelableExtra<EpisodeInfo>(Const.EXTRA_EPISODE)
        )
    }

    private val customTitleColor: Int by lazyNone {
        intent.getIntExtra(Const.EXTRA_MAIN_COLOR, Color.BLACK)
    }

    private var SWIPE_MIN_DISTANCE: Int = 0
    private var SWIPE_THRESHOLD_VELOCITY: Int = 0
    private var statusBarAnimator: ObjectAnimator? = null

    private val DELAY_TIME = TimeUnit.MILLISECONDS.convert(3, TimeUnit.SECONDS)

    private val dlg: ProgressDialog by lazyNone {
        ProgressDialog(this).apply {
            setMessage(getString(R.string.msg_loading))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setSupportActionBar(toolbar_actionbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initView()
        fragmentInit()

        resources.displayMetrics.apply {
            SWIPE_MIN_DISTANCE = widthPixels / 3
            SWIPE_THRESHOLD_VELOCITY = widthPixels / 2
        }
    }

    private fun initView() {
        tvSubTitle.text = ""
        btnPrev.isEnabled = false
        btnNext.isEnabled = false

        AnimatorSet().apply {
            playTogether(bgColorAnimator(), getStatusBarAnimator())
            duration = 1000L
            interpolator = DecelerateInterpolator()
            start()
        }

        btnPrev.setOnClickListener { viewModel.movePrev() }
        btnNext.setOnClickListener { viewModel.moveNext() }

        viewModel.event.observeNonNull(this) { event ->
            when (event) {
                DetailEvent.START -> dlg.show()
                DetailEvent.LOADED -> dlg.dismiss()
                is DetailEvent.ERROR -> {
                    dlg.dismiss()
                    showError(event)
                }
                is DetailEvent.SHARE -> {
                    showShare(event.item)
                }
            }
        }
        viewModel.elementEvent.observeNonNull(this) { event ->
            tvTitle.text = event.title
            tvSubTitle.text = event.webToonTitle
            btnPrev.isEnabled = event.isPrevEnable
            btnNext.isEnabled = event.isNextEnable
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
        statusBarAnimator =
                ObjectAnimator.ofInt(window, "statusBarColor", resValue.data, customTitleColor)
                    .apply {
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
            val state = arrayOf(
                intArrayOf(-android.R.attr.state_enabled),
                intArrayOf(android.R.attr.state_pressed),
                intArrayOf(android.R.attr.state_enabled)
            )
            val colors = intArrayOf(Color.WHITE, customTitleColor, Color.WHITE)
            return ColorStateList(state, colors)
        }

    private fun fragmentInit() {
        supportFragmentManager.commit {
            replace(R.id.container, DetailFragment(bottomMenu.height))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }

        when (item.itemId) {
            R.id.menu_item_share -> {
                // 공유하기
                viewModel.requestShare()
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
//        val currentItem = currentItem
//        if (currentItem?.list?.isNotEmpty() == true) {
//            fragmentAttach(currentItem.list)
//        }
    }

    ///////////////////////////////////////////////////////////////////////////
    //
    ///////////////////////////////////////////////////////////////////////////

    private fun showError(event: DetailEvent.ERROR) {
        AlertDialog.Builder(this@DetailActivity)
            .setMessage(event.errorType.getMessage(this))
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                supportFragmentManager.findFragmentByTag(Const.DETAIL_FRAG_TAG) ?: finish()
            }
            .show()
    }

    private fun showShare(item: ShareItem) {
        startActivity(
            Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, item.title)
                putExtra(Intent.EXTRA_TEXT, item.url)
            }, "Share")
        )
    }
}
