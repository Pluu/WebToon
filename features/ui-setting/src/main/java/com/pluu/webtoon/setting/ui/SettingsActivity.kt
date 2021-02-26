package com.pluu.webtoon.setting.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import com.pluu.compose.ambient.LocalPreferenceProvider
import com.pluu.compose.ambient.PreferenceProvider
import com.pluu.webtoon.navigator.SettingNavigator
import com.pluu.webtoon.ui.compose.fragmentComposeView
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
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
    lateinit var preferenceProvider: PreferenceProvider

    @Inject
    lateinit var navigator: SettingNavigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = fragmentComposeView {
        ProvideWindowInsets {
            val context = LocalContext.current
            CompositionLocalProvider(LocalPreferenceProvider provides preferenceProvider) {
                SettingContentUi(
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
