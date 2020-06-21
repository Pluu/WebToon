package com.pluu.webtoon.setting.ui

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.pluu.utils.viewbinding.viewBinding
import com.pluu.webtoon.setting.R
import com.pluu.webtoon.setting.databinding.ActivityLicenseBinding

/**
 * License Activity
 * Created by pluu on 2017-05-05.
 */
class LicenseActivity : AppCompatActivity(R.layout.activity_license) {

    private val binding by viewBinding(ActivityLicenseBinding::bind)

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initView()
    }

    private fun initView() {
        binding.listView.apply {
            layoutManager = LinearLayoutManager(this@LicenseActivity)
            adapter = LicenseAdapter(this@LicenseActivity, this@LicenseActivity::showDetailLicense)
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
     * Show Detail License Page
     * @param url Web Site Url
     */
    private fun showDetailLicense(url: String) {
        // http://qiita.com/droibit/items/66704f96a602adec5a35
        CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setToolbarColor(ContextCompat.getColor(this, R.color.theme_primary))
            .build()
            .launchUrl(this, Uri.parse(url))
    }
}
