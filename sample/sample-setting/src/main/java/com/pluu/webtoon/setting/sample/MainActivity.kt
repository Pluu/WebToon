package com.pluu.webtoon.setting.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pluu.utils.startActivity
import com.pluu.webtoon.setting.ui.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity<SettingsActivity>()
        finish()
    }
}
