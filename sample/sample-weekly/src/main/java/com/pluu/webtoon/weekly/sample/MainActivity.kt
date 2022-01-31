package com.pluu.webtoon.weekly.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pluu.utils.startActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity<WeeklyActivity>()
        finish()
    }
}
