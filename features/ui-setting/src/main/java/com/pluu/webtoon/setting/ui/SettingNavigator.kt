package com.pluu.webtoon.setting.ui

import android.content.Context
import com.pluu.utils.startActivity
import com.pluu.webtoon.navigator.SettingNavigator
import javax.inject.Inject

class SettingNavigatorImpl @Inject constructor() : SettingNavigator {
    override fun openSetting(context: Context) {
        context.startActivity<SettingsActivity>()
    }

    override fun openLicense(context: Context) {
        context.startActivity<LicenseActivity>()
    }
}