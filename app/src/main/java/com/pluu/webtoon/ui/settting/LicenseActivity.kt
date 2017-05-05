package com.pluu.webtoon.ui.settting

import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.pluu.event.RxBusProvider
import com.pluu.webtoon.R
import com.pluu.webtoon.adapter.LicenseAdapter
import com.pluu.webtoon.event.RecyclerViewEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_license.*

/**
 * License Activity
 * Created by pluu on 2017-05-05.
 */
class LicenseActivity : AppCompatActivity() {

    private var mCompositeDisposable = CompositeDisposable()

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
        mCompositeDisposable.add(
                RxBusProvider.getInstance()
                        .toObservable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(busEvent)
        )
    }

    public override fun onPause() {
        super.onPause()
        mCompositeDisposable.clear()
    }

    private val busEvent = Consumer<Any> {
        when (it) {
            is RecyclerViewEvent -> itemClick(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
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
