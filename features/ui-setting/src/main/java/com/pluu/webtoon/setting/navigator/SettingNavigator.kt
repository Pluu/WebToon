package com.pluu.webtoon.setting.navigator

import android.content.Context
import com.pluu.utils.startActivity
import com.pluu.webtoon.navigator.SettingNavigator
import com.pluu.webtoon.setting.ui.LicenseActivity
import com.pluu.webtoon.setting.ui.SettingsActivity
import javax.inject.Inject

internal class SettingNavigatorImpl @Inject constructor() : SettingNavigator {
    override fun openSetting(context: Context) {
        context.startActivity<SettingsActivity>()
    }

    override fun openLicense(context: Context) {
        context.startActivity<LicenseActivity>()
    }
}