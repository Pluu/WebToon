package com.pluu.webtoon.setting.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import com.google.accompanist.insets.ProvideWindowInsets
import com.pluu.compose.ambient.ProvidePreference
import com.pluu.webtoon.navigator.SettingNavigator
import com.pluu.webtoon.ui.compose.fragmentComposeView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val content = FrameLayout(this).apply {
            id = View.generateViewId()
        }
        setContentView(content)

        supportFragmentManager.commit {
            replace(content.id, SettingFragment())
        }
    }
}

@AndroidEntryPoint
class SettingFragment : Fragment() {
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var navigator: SettingNavigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = fragmentComposeView {
        ProvideWindowInsets {
            val context = LocalContext.current
            ProvidePreference(sharedPreferences) {
                SettingsScreen(
                    onBackPressed = {
                        activity?.finish()
                    },
                    onOpenSourceClicked = {
                        navigator.openLicense(context)
                    }
                )
            }
        }
    }
}
