package com.pluu.webtoon.ui.settting

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.pluu.event.EventBus
import com.pluu.webtoon.R
import com.pluu.webtoon.adapter.LicenseAdapter
import com.pluu.webtoon.event.RecyclerViewEvent
import com.pluu.webtoon.utils.launchWithUI
import com.pluu.webtoon.utils.lazyNone
import kotlinx.android.synthetic.main.activity_license.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.consumeEach
import kotlin.coroutines.CoroutineContext

/**
 * License Activity
 * Created by pluu on 2017-05-05.
 */
class LicenseActivity : AppCompatActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private val job by lazyNone { Job() }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_license)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initView()
    }

    private fun initView() {
        listView?.apply {
            layoutManager = LinearLayoutManager(this@LicenseActivity)
            adapter = LicenseAdapter(this@LicenseActivity)
        }
    }

    public override fun onResume() {
        super.onResume()
        launchWithUI {
            registerViewEvent()
        }
    }

    override fun onPause() {
        super.onPause()
        job.cancel()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private suspend fun registerViewEvent() {
        EventBus.subscribeToEvent<RecyclerViewEvent>()
            .consumeEach {
                itemClick(it)
            }
    }

    /**
     * RecyclerView Item Click
     * @param event Click Event
     */
    private fun itemClick(event: RecyclerViewEvent) {
        val url = resources.getStringArray(R.array.license_url)[event.pos]

        // http://qiita.com/droibit/items/66704f96a602adec5a35

        CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setToolbarColor(ContextCompat.getColor(this, R.color.theme_primary))
            .build()
            .launchUrl(this, Uri.parse(url))
    }
}
