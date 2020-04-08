package com.pluu.webtoon.ui.detail

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.pluu.webtoon.R
import com.pluu.webtoon.common.Const
import com.pluu.webtoon.databinding.ActivityDetailBinding
import com.pluu.webtoon.domain.moel.EpisodeInfo
import com.pluu.webtoon.domain.moel.ShareItem
import com.pluu.webtoon.ui.weekly.PalletColor
import com.pluu.webtoon.utils.getMessage
import com.pluu.webtoon.utils.lazyNone
import com.pluu.webtoon.utils.observeNonNull
import com.pluu.webtoon.utils.resolveAttribute
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

    private lateinit var binding: ActivityDetailBinding

    private val palletColor by lazyNone {
        intent.getParcelableExtra<PalletColor>(Const.EXTRA_PALLET)!!
    }

    private var SWIPE_MIN_DISTANCE: Int = 0
    private var SWIPE_THRESHOLD_VELOCITY: Int = 0
    private var statusBarAnimator: ObjectAnimator? = null

    private val DELAY_TIME = TimeUnit.MILLISECONDS.convert(3, TimeUnit.SECONDS)

    private val dlg by lazyNone {
        ProgressDialog(this).apply {
            setMessage(getString(R.string.msg_loading))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarActionbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initView()
        fragmentInit()

        resources.displayMetrics.apply {
            SWIPE_MIN_DISTANCE = widthPixels / 3
            SWIPE_THRESHOLD_VELOCITY = widthPixels / 2
        }
    }

    private fun initView() {
        binding.tvSubTitle.text = ""
        binding.btnPrev.isEnabled = false
        binding.btnNext.isEnabled = false

        AnimatorSet().apply {
            playTogether(bgColorAnimator(), getStatusBarAnimator())
            duration = 1000L
            interpolator = DecelerateInterpolator()
            start()
        }

        binding.btnPrev.setOnClickListener { viewModel.movePrev() }
        binding.btnNext.setOnClickListener { viewModel.moveNext() }

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
            binding.tvTitle.text = event.title
            binding.tvSubTitle.text = event.webToonTitle
            binding.btnPrev.isEnabled = event.prevEpisodeId.isNullOrEmpty().not()
            binding.btnNext.isEnabled = event.nextEpisodeId.isNullOrEmpty().not()
        }
    }

    private fun bgColorAnimator(): Animator {
        val value = resolveAttribute(R.attr.colorPrimary)

        return ValueAnimator.ofObject(ArgbEvaluator(), value.data, palletColor.darkVibrantColor)
            .apply {
                addUpdateListener { animation ->
                    val value1 = animation.animatedValue as Int
                    binding.toolbarActionbar.setBackgroundColor(value1)
                    binding.btnPrev.backgroundTintList = stateListBgDrawable(value1)
                    binding.btnNext.backgroundTintList = stateListBgDrawable(value1)
                }
            }
    }

    private fun getStatusBarAnimator(): Animator {
        statusBarAnimator?.cancel()
        val resValue = resolveAttribute(R.attr.colorPrimaryDark)
        statusBarAnimator =
            ObjectAnimator.ofInt(
                window,
                "statusBarColor",
                resValue.data,
                palletColor.darkVibrantColor
            ).apply {
                setEvaluator(ArgbEvaluator())
            }
        return statusBarAnimator!!
    }

    private fun stateListBgDrawable(color: Int): ColorStateList = ColorStateList(
        arrayOf(
            intArrayOf(-android.R.attr.state_enabled),
            intArrayOf(android.R.attr.state_enabled)
        ),
        intArrayOf(
            Color.GRAY,
            color
        )
    )

    private fun fragmentInit() {
        supportFragmentManager.commit {
            replace(R.id.container, DetailFragment(binding.bottomMenu.height))
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
            moveToAxisY(binding.toolbarActionbar, true)
            moveToAxisY(binding.bottomMenu, false)
        } else {
            moveRevert(binding.toolbarActionbar)
            moveRevert(binding.bottomMenu)
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
        val toggleId = 0
        mToggleHandler.removeMessages(toggleId)
        mToggleHandler.sendEmptyMessageDelayed(toggleId, if (isDelay) DELAY_TIME else 0)
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

    // /////////////////////////////////////////////////////////////////////////
    //
    // /////////////////////////////////////////////////////////////////////////

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
