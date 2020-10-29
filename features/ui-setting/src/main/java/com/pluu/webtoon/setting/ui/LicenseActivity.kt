package com.pluu.webtoon.setting.ui

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.pluu.webtoon.setting.R
import com.pluu.webtoon.setting.licenseModels
import com.pluu.webtoon.setting.model.LicenseModel
import com.pluu.webtoon.ui.compose.ActivityComposeView

/**
 * License Activity
 * Created by pluu on 2017-05-05.
 */
class LicenseActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityComposeView {
            LicenseContentUi(
                list = licenseModels,
                onBackPressed = { finish() },
                onClicked = ::showDetailLicense
            )
        }
    }

    /**
     * Show Detail License Page
     * @param item License Model
     */
    private fun showDetailLicense(item: LicenseModel) {
        // http://qiita.com/droibit/items/66704f96a602adec5a35
        CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setDefaultColorSchemeParams(
                CustomTabColorSchemeParams.Builder()
                    .setToolbarColor(ContextCompat.getColor(this, R.color.theme_primary))
                    .build()
            )
            .build()
            .launchUrl(this, Uri.parse(item.url))
    }
}
