package com.pluu.webtoon.ui.detail

import android.animation.Animator
import android.app.Activity
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
import androidx.lifecycle.SavedStateHandle
import com.pluu.webtoon.R
import com.pluu.webtoon.common.Const
import com.pluu.webtoon.databinding.ActivityDetailBinding
import com.pluu.webtoon.domain.moel.EpisodeInfo
import com.pluu.webtoon.domain.moel.ShareItem
import com.pluu.webtoon.ui.weekly.PalletColor
import com.pluu.webtoon.utils.ProgressDialog
import com.pluu.webtoon.utils.animatorColor
import com.pluu.webtoon.utils.getMessage
import com.pluu.webtoon.utils.getRequiredParcelableExtra
import com.pluu.webtoon.utils.getThemeColor
import com.pluu.webtoon.utils.lazyNone
import com.pluu.webtoon.utils.observeNonNull
import com.pluu.webtoon.utils.setStatusBarColor
import com.pluu.webtoon.utils.viewbinding.viewBinding
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.concurrent.TimeUnit

/**
 * 상세화면 Activity
 * Created by pluu on 2017-05-09.
 */
class DetailActivity : AppCompatActivity(R.layout.activity_detail),
    ToggleListener, FirstBindListener {

    private val viewModel: DetailViewModel by viewModel {
        parametersOf(
            SavedStateHandle(
                mapOf(Const.EXTRA_EPISODE to intent.getParcelableExtra<EpisodeInfo>(Const.EXTRA_EPISODE))
            )
        )
    }

    private val binding by viewBinding(ActivityDetailBinding::bind)

    private val palletColor by lazyNone {
        intent.getRequiredParcelableExtra<PalletColor>(Const.EXTRA_PALLET)
    }

    private val toggleDelayTime = TimeUnit.MILLISECONDS.toMillis(150)
    private val toggleAnimTime = 200L

    private val toggleId = 0

    private val dlg by lazyNone {
        ProgressDialog.create(this, R.string.msg_loading)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbarActionbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initView()
        fragmentInit()
    }

    private fun initView() {
        variantAnimator().start()

        binding.tvSubTitle.text = ""
        binding.btnPrev.isEnabled = false
        binding.btnNext.isEnabled = false

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

    private fun variantAnimator(): Animator = animatorColor(
        startColor = getThemeColor(R.attr.colorPrimary),
        endColor = palletColor.darkVibrantColor
    ).apply {
        duration = 1000L
        interpolator = DecelerateInterpolator()
        addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            binding.toolbarActionbar.setBackgroundColor(value)
            binding.btnPrev.backgroundTintList = stateListBgDrawable(value)
            binding.btnNext.backgroundTintList = stateListBgDrawable(value)

            this@DetailActivity.setStatusBarColor(value)
        }
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
            replace(R.id.container, DetailFragment())
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
            .setDuration(toggleAnimTime)
            .translationY((if (isToTop) -view.height else view.height).toFloat())
            .start()
    }

    private fun moveRevert(view: View) {
        view.animate()
            .setDuration(toggleAnimTime)
            .translationY(0f)
            .start()
    }

    private fun toggleDelay(isDelay: Boolean) {
        mToggleHandler.removeMessages(toggleId)
        mToggleHandler.sendEmptyMessageDelayed(toggleId, if (isDelay) toggleDelayTime else 0)
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
