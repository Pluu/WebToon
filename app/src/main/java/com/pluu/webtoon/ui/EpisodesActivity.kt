package com.pluu.webtoon.ui

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.pluu.event.RxBusProvider
import com.pluu.support.impl.NAV_ITEM
import com.pluu.webtoon.AppController
import com.pluu.webtoon.R
import com.pluu.webtoon.common.Const
import com.pluu.webtoon.db.RealmHelper
import com.pluu.webtoon.event.FirstItemSelectEvent
import com.pluu.webtoon.item.WebToonInfo
import com.pluu.webtoon.utils.animatorToolbarColor
import com.pluu.webtoon.utils.setStatusBarColor
import kotlinx.android.synthetic.main.activity_episode.*
import kotlinx.android.synthetic.main.toolbar_actionbar.*
import javax.inject.Inject

/**
 * 에피소드 리스트 Activity
 * Created by pluu on 2017-05-09.
 */
class EpisodesActivity : AppCompatActivity() {

    @Inject
    lateinit var realmHelper: RealmHelper

    private var childTitle: View? = null

    private lateinit var webToonInfo: WebToonInfo
    private var customTitleColor: Int = 0
    private var customStatusColor: Int = 0
    private lateinit var service: NAV_ITEM

    private var isEdit = false
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_episode)
        (applicationContext as AppController).realmHelperComponent.inject(this)

        setSupportActionBar(toolbar_actionbar)

        webToonInfo = intent.getParcelableExtra<WebToonInfo>(Const.EXTRA_EPISODE).apply {
            isFavorite = isFavorite
        }

        initSupportActionBar()
        getApi()
        initView()
        initFragment()
    }

    private fun initSupportActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = webToonInfo.title
        }
    }

    private fun getApi() {
        service = intent.getSerializableExtra(Const.EXTRA_API) as NAV_ITEM
    }

    private fun initView() {
        customTitleColor = intent.getIntExtra(Const.EXTRA_MAIN_COLOR, Color.BLACK)
        customStatusColor = intent.getIntExtra(Const.EXTRA_STATUS_COLOR, Color.BLACK)

        // Title TextView
        for (i in 0 until toolbar_actionbar.childCount) {
            if (toolbar_actionbar.getChildAt(i) is TextView) {
                childTitle = toolbar_actionbar.getChildAt(i)
                break
            }
        }

        val listener = ValueAnimator.AnimatorUpdateListener { animation ->
            val value = animation.animatedValue as Int
            toolbar_actionbar.setBackgroundColor(value)
            btnFirst.setBackgroundColor(value)
            childTitle?.setBackgroundColor(value)
            tvName.setTextColor(value)
            tvRate.setTextColor(value)

            this@EpisodesActivity.setStatusBarColor(value)
        }

        animatorToolbarColor(customTitleColor, listener).let {
            it.duration = 1000L
            it.start()
        }

        tvName.text = webToonInfo.writer
        if (!TextUtils.isEmpty(webToonInfo.rate)) {
            tvRate.text = webToonInfo.rate
            tvRate.visibility = View.VISIBLE
        }

        btnFirst.setOnClickListener {
            RxBusProvider.getInstance().send(FirstItemSelectEvent())
        }
    }

    private fun initFragment() {
        val fragment = EpisodeFragment.newInstance(service, webToonInfo,
                intArrayOf(customTitleColor, customStatusColor))

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment, Const.MAIN_FRAG_TAG)
                .commit()
    }

    override fun finish() {
        if (isEdit) {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(Const.EXTRA_EPISODE, webToonInfo)
            })
        }
        super.finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_episode, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val ret = super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.menu_item_favorite_add).isVisible = !isFavorite
        menu.findItem(R.id.menu_item_favorite_delete).isVisible = isFavorite
        return ret
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }

        when (item.itemId) {
            R.id.menu_item_favorite_add -> {
                // 즐겨찾기 추가
                realmHelper.addFavorite(service, webToonInfo.toonId)
                setFavorite(true)
            }
            R.id.menu_item_favorite_delete -> {
                // 즐겨찾기 삭제
                realmHelper.removeFavorite(service, webToonInfo.toonId)
                setFavorite(false)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setFavorite(isFavorite: Boolean) {
        isEdit = true
        this.isFavorite = isFavorite
        webToonInfo.isFavorite = isFavorite

        Toast.makeText(this,
                if (isFavorite) R.string.favorite_add else R.string.favorite_delete,
                Toast.LENGTH_SHORT).show()
    }

}
